tlens.actividad3.ui.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartlens.actividad3.R
import com.smartlens.actividad3.data.models.FormData
import com.smartlens.actividad3.ui.auth.AuthViewModel
import com.smartlens.actividad3.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val titleResId: Int, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", R.string.dashboard, Icons.Default.Dashboard)
    object Form : Screen("form", R.string.data_entry, Icons.Default.AddBox)
    object Profile : Screen("profile", R.string.user_profile, Icons.Default.AccountCircle)
    object Settings : Screen("settings", R.string.preferences, Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    viewModel: AuthViewModel,
    themeViewModel: ThemeViewModel,
    dataViewModel: DataViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val formDataList by dataViewModel.formDataList.collectAsState()

    // Form states
    var nameInput by remember { mutableStateOf("") }
    var detailInput by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    
    val systemsOperationalMsg = stringResource(R.string.systems_operational, formDataList.size)
    val recordCommittedMsg = stringResource(R.string.record_committed)
    val mandatoryFieldMsg = stringResource(R.string.mandatory_field)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Business,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.enterprise_hub),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    userName?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
                
                val items = listOf(Screen.Dashboard, Screen.Form, Screen.Profile, Screen.Settings)
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(stringResource(item.titleResId), fontWeight = if (item == selectedScreen) FontWeight.Bold else FontWeight.Normal) },
                        selected = item == selectedScreen,
                        onClick = {
                            selectedScreen = item
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }   
                
                Spacer(modifier = Modifier.weight(1f))
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Logout, contentDescription = null) },
                    label = { Text(stringResource(R.string.sign_out)) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            viewModel.logout()
                            onLogout()
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            text = stringResource(selectedScreen.titleResId),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.open_menu))
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(systemsOperationalMsg)
                            }
                        }) {
                            BadgedBox(
                                badge = { if (formDataList.isNotEmpty()) Badge { Text(formDataList.size.toString()) } }
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = stringResource(R.string.notifications))
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {
                when (selectedScreen) {
                    Screen.Dashboard -> {
                        DashboardContent(userName, formDataList)
                    }
                    Screen.Form -> {
                        FormContent(
                            nameInput = nameInput,
                            onNameChange = { 
                                nameInput = it
                                if (it.isNotBlank()) nameError = null
                            },
                            detailInput = detailInput,
                            onDetailChange = { detailInput = it },
                            nameError = nameError,
                            onSave = {
                                if (nameInput.isBlank()) {
                                    nameError = mandatoryFieldMsg
                                } else {
                                    dataViewModel.saveData(nameInput, detailInput)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(recordCommittedMsg)
                                        nameInput = ""
                                        detailInput = ""
                                        selectedScreen = Screen.Dashboard
                                    }
                                }
                            }
                        )
                    }
                    Screen.Profile -> {
                        ProfileContent(userName, userEmail)
                    }
                    Screen.Settings -> {
                        SettingsContent(themeViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardContent(userName: String?, dataList: List<FormData>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.status_overview),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.welcome_back, userName ?: stringResource(R.string.administrator)),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = stringResource(R.string.recent_records),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (dataList.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.no_data_available),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(dataList) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = MaterialTheme.shapes.medium,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Description,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item.title, 
                                    style = MaterialTheme.typography.titleMedium, 
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (item.details.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item.details, 
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormContent(
    nameInput: String,
    onNameChange: (String) -> Unit,
    detailInput: String,
    onDetailChange: (String) -> Unit,
    nameError: String?,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.data_acquisition),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.input_structured_info),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        OutlinedTextField(
            value = nameInput,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.record_identifier)) },
            modifier = Modifier.fillMaxWidth(),
            isError = nameError != null,
            supportingText = { if (nameError != null) Text(nameError) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = detailInput,
            onValueChange = onDetailChange,
            label = { Text(stringResource(R.string.detailed_description)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.CloudUpload, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(stringResource(R.string.commit_data_record), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun ProfileContent(userName: String?, userEmail: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(140.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = userName ?: stringResource(R.string.authorized_user), 
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = userEmail ?: stringResource(R.string.no_verified_email), 
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        ListItem(
            headlineContent = { Text(stringResource(R.string.account_security)) },
            trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) }
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        ListItem(
            headlineContent = { Text(stringResource(R.string.role_management)) },
            trailingContent = { Text(stringResource(R.string.administrator), style = MaterialTheme.typography.bodySmall) }
        )
    }
}

@Composable
fun SettingsContent(themeViewModel: ThemeViewModel) {
    val isDarkModePref by themeViewModel.isDarkMode.collectAsState()
    val isDarkMode = isDarkModePref ?: isSystemInDarkTheme()

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.interface_preferences),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.enterprise_dark_mode), fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text(stringResource(R.string.dark_mode_desc)) },
                    trailingContent = { 
                        Switch(
                            checked = isDarkMode, 
                            onCheckedChange = { themeViewModel.toggleTheme(it) }
                        ) 
                    },
                    colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                ListItem(
                    headlineContent = { Text(stringResource(R.string.system_notifications), fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text(stringResource(R.string.notifications_desc)) },
                    trailingContent = { Switch(checked = true, onCheckedChange = {}) },
                    colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
                )
            }
        }
    }
}
