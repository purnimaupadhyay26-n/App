package com.maira.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blush = Color(0xFFF7C6D0)
private val Peach = Color(0xFFF6B99B)
private val Butter = Color(0xFFFFE7A3)
private val WarmWhite = Color(0xFFFFFBF7)
private val Plum = Color(0xFF442D38)
private val Muted = Color(0xFF806B73)
private val Rose = Color(0xFFB94F76)
private val PaleLilac = Color(0xFFF0E8F8)
private val PaleMint = Color(0xFFE6F3E8)
private val BorderSoft = Color(0xFFF0E1E4)

private data class Role(val title: String, val subtitle: String, val icon: ImageVector)
private data class ActionItem(val title: String, val subtitle: String, val icon: ImageVector, val tint: Color)

private val roles = listOf(
    Role("Patient / Mother", "Pregnancy, baby care and referral help", Icons.Filled.Favorite),
    Role("ASHA Worker", "Mothers, visits and referrals", Icons.Filled.Groups),
    Role("Ambulance Driver", "Assigned trips and arrival updates", Icons.Filled.Emergency),
    Role("Doctor / Hospital Staff", "Review referrals and confirm handover", Icons.Filled.LocalHospital)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(
                primary = Rose, onPrimary = Color.White, secondary = Peach,
                background = WarmWhite, surface = Color.White, onSurface = Plum
            )) { MairaApp() }
        }
    }
}

@Composable
private fun MairaApp() {
    var selectedRole by remember { mutableStateOf<Role?>(null) }
    var signedInRole by remember { mutableStateOf<Role?>(null) }
    when {
        signedInRole != null -> DashboardScreen(signedInRole!!, onSignOut = {
            signedInRole = null; selectedRole = null
        })
        selectedRole != null -> LoginScreen(selectedRole!!, onBack = { selectedRole = null }) {
            signedInRole = selectedRole
        }
        else -> WelcomeScreen { selectedRole = it }
    }
}

@Composable
private fun WelcomeScreen(onRoleSelected: (Role) -> Unit) {
    var language by remember { mutableStateOf("English") }
    var expanded by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(WarmWhite, Color(0xFFFFF0F2), Color(0xFFFFF7E7))))) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 22.dp, vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(8.dp))
            Box(Modifier.size(72.dp).background(Brush.linearGradient(listOf(Blush, Peach)), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Favorite, contentDescription = "MAIRA", tint = Plum, modifier = Modifier.size(34.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text("MAIRA", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Plum, letterSpacing = 2.sp)
            Text("Maternal & Newborn Care Support", fontSize = 14.sp, color = Muted, textAlign = TextAlign.Center)
            Spacer(Modifier.height(22.dp))
            Surface(color = Color.White.copy(alpha = .92f), shape = RoundedCornerShape(26.dp), shadowElevation = 3.dp, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    Text("Care, connected.", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Plum)
                    Text("The right support for every step.", color = Muted, fontSize = 14.sp, modifier = Modifier.padding(top = 5.dp, bottom = 15.dp))
                    Text("CHOOSE LANGUAGE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Muted, letterSpacing = 1.sp)
                    Box {
                        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp)) {
                            Icon(Icons.Filled.Language, null); Spacer(Modifier.width(8.dp)); Text(language); Spacer(Modifier.weight(1f)); Icon(Icons.Filled.ExpandMore, null)
                        }
                        DropdownMenu(expanded, { expanded = false }) {
                            listOf("English", "हिन्दी", "ગુજરાતી", "मराठी").forEach { lang ->
                                DropdownMenuItem(text = { Text(lang) }, onClick = { language = lang; expanded = false })
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(22.dp))
            Text("CONTINUE AS", modifier = Modifier.fillMaxWidth(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Muted, letterSpacing = 1.4.sp)
            Spacer(Modifier.height(10.dp))
            roles.forEachIndexed { i, role ->
                RoleCard(role, i) { onRoleSelected(role) }
                Spacer(Modifier.height(11.dp))
            }
            Surface(color = Butter.copy(alpha = .55f), shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Info, null, tint = Plum); Spacer(Modifier.width(9.dp))
                    Text("DEMO PROTOTYPE · No real emergency service connected.", fontSize = 12.sp, color = Plum)
                }
            }
        }
    }
}

@Composable
private fun RoleCard(role: Role, index: Int, onClick: () -> Unit) {
    val tints = listOf(Blush, Color(0xFFF9DCCB), Butter, PaleMint)
    Surface(onClick = onClick, color = Color.White.copy(alpha = .96f), shape = RoundedCornerShape(21.dp), shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(54.dp).background(tints[index], RoundedCornerShape(17.dp)), contentAlignment = Alignment.Center) {
                Icon(role.icon, role.title, tint = Plum, modifier = Modifier.size(27.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(role.title, fontWeight = FontWeight.Bold, color = Plum, fontSize = 16.sp)
                Text(role.subtitle, color = Muted, fontSize = 12.sp, lineHeight = 16.sp, modifier = Modifier.padding(top = 3.dp))
            }
            Icon(Icons.Filled.ChevronRight, "Continue", tint = Rose, modifier = Modifier.size(25.dp))
        }
    }
}

@Composable
private fun LoginScreen(role: Role, onBack: () -> Unit, onDemoSignIn: () -> Unit) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(WarmWhite, Color(0xFFFFEFF2))))) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(22.dp)) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Plum) }
            Spacer(Modifier.height(10.dp))
            Box(Modifier.size(64.dp).background(Blush, CircleShape), contentAlignment = Alignment.Center) { Icon(role.icon, null, tint = Plum, modifier = Modifier.size(30.dp)) }
            Spacer(Modifier.height(18.dp))
            Text("Welcome back", fontSize = 29.sp, fontWeight = FontWeight.Bold, color = Plum)
            Text(role.title, fontSize = 16.sp, color = Rose, fontWeight = FontWeight.SemiBold)
            Text("Sign in to continue to your workspace.", color = Muted, fontSize = 14.sp, modifier = Modifier.padding(top = 7.dp, bottom = 18.dp))
            Surface(color = Butter.copy(alpha = .6f), shape = RoundedCornerShape(12.dp)) {
                Text("DEMO MODE · No real OTP or account", Modifier.padding(horizontal = 12.dp, vertical = 9.dp), color = Plum, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
            Spacer(Modifier.height(18.dp))
            Text("Mobile number", color = Plum, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(phone, { phone = it.filter(Char::isDigit).take(10); error = "" }, modifier = Modifier.fillMaxWidth().padding(top = 6.dp), placeholder = { Text("10-digit mobile number") }, leadingIcon = { Icon(Icons.Filled.Phone, null) }, singleLine = true, shape = RoundedCornerShape(16.dp))
            Spacer(Modifier.height(13.dp))
            Text("Demo password", color = Plum, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(password, { password = it; error = "" }, modifier = Modifier.fillMaxWidth().padding(top = 6.dp), placeholder = { Text("Enter any 4+ characters") }, leadingIcon = { Icon(Icons.Filled.Lock, null) }, trailingIcon = { IconButton(onClick = { visible = !visible }) { Icon(if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, "Toggle password") } }, visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(), singleLine = true, shape = RoundedCornerShape(16.dp))
            if (error.isNotBlank()) Text(error, color = Color(0xFFB3261E), fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))
            Spacer(Modifier.height(20.dp))
            Button(onClick = {
                error = when { phone.length != 10 -> "Please enter a valid 10-digit mobile number."; password.length < 4 -> "Enter at least 4 characters."; else -> "" }
                if (error.isBlank()) onDemoSignIn()
            }, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(17.dp), colors = ButtonDefaults.buttonColors(containerColor = Rose)) {
                Text("Continue to demo", fontWeight = FontWeight.Bold, fontSize = 16.sp); Spacer(Modifier.width(8.dp)); Icon(Icons.Filled.ArrowForward, null)
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = onDemoSignIn, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(17.dp)) { Icon(Icons.Filled.Visibility, null); Spacer(Modifier.width(8.dp)); Text("Explore without signing in") }
            Text("Do not enter real patient information.", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreen(role: Role, onSignOut: () -> Unit) {
    var tab by remember { mutableStateOf("Home") }
    var activeAction by remember { mutableStateOf<String?>(null) }
    var showChat by remember { mutableStateOf(false) }
    var chatText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = WarmWhite,
        topBar = {
            TopAppBar(
                title = { Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(38.dp).background(Blush, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Filled.Favorite, null, tint = Plum, modifier = Modifier.size(20.dp)) }
                    Spacer(Modifier.width(10.dp))
                    Column { Text("MAIRA", color = Plum, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp); Text(role.title, color = Muted, fontSize = 11.sp) }
                } },
                actions = { IconButton(onClick = { activeAction = "Notifications" }) { Icon(Icons.Filled.NotificationsNone, "Notifications", tint = Plum) }; TextButton(onClick = onSignOut) { Text("Sign out", color = Rose) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmWhite)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                listOf("Home", "Care", "Chat", "Profile").forEach { t ->
                    NavigationBarItem(selected = tab == t, onClick = { tab = t; if (t == "Chat") showChat = true else if (t != "Home") activeAction = t }, icon = {
                        Icon(when (t) { "Home" -> Icons.Filled.Home; "Care" -> Icons.Filled.FavoriteBorder; "Chat" -> Icons.Filled.ChatBubbleOutline; else -> Icons.Filled.Person }, t)
                    }, label = { Text(t) })
                }
            }
        },
        floatingActionButton = {
            if (role.title == "Patient / Mother") {
                FloatingActionButton(onClick = { showChat = true }, containerColor = Rose, contentColor = Color.White) { Icon(Icons.Filled.Chat, "Open care assistant") }
            }
        }
    ) { padding ->
        when (role.title) {
            "Patient / Mother" -> MotherHome(Modifier.padding(padding), onAction = { activeAction = it })
            "ASHA Worker" -> AshaHome(Modifier.padding(padding), onAction = { activeAction = it })
            "Ambulance Driver" -> DriverHome(Modifier.padding(padding), onAction = { activeAction = it })
            else -> HospitalHome(Modifier.padding(padding), onAction = { activeAction = it })
        }
    }

    if (activeAction != null) {
        AlertDialog(onDismissRequest = { activeAction = null }, containerColor = WarmWhite,
            icon = { Icon(Icons.Filled.AutoAwesome, null, tint = Rose) },
            title = { Text(activeAction!!, color = Plum) },
            text = { Text("Interactive UI preview. Detailed workflow and backend connection will be added in the next phase.", color = Muted) },
            confirmButton = { TextButton(onClick = { activeAction = null }) { Text("Got it", color = Rose) } })
    }
    if (showChat) {
        AlertDialog(onDismissRequest = { showChat = false }, containerColor = WarmWhite,
            icon = { Icon(Icons.Filled.Forum, null, tint = Rose) },
            title = { Text("MAIRA Care Assistant", color = Plum) },
            text = { Column {
                Text("Hello! I can help you find sections in this demo. For an emergency, contact local emergency services or a health worker directly.", color = Muted, fontSize = 13.sp)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(chatText, { chatText = it }, placeholder = { Text("Type your question…") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                if (chatText.isNotBlank()) Text("Demo reply: Please contact your ASHA worker for personal care guidance.", color = Rose, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            } },
            confirmButton = { TextButton(onClick = { chatText = ""; showChat = false }) { Text("Close", color = Rose) } })
    }
}

@Composable
private fun MotherHome(modifier: Modifier, onAction: (String) -> Unit) {
    var selectedPeriod by remember { mutableStateOf("Pregnancy") }
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Namaste, Ananya 👋", color = Muted, fontSize = 13.sp)
                Text("Your care space", color = Plum, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
            Surface(color = Butter.copy(alpha = .65f), shape = CircleShape) { IconButton(onClick = { onAction("Reminders") }) { Icon(Icons.Filled.NotificationsActive, "Reminders", tint = Plum) } }
        }
        Spacer(Modifier.height(14.dp))
        Surface(color = Color.White, shape = RoundedCornerShape(24.dp), shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.background(Brush.linearGradient(listOf(Color(0xFFFFE7EA), Color.White, Color(0xFFFFF1D2)))).padding(17.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Pregnancy journey", color = Muted, fontSize = 12.sp)
                        Text("Week 9", color = Plum, fontSize = 29.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Every step matters.", color = Rose, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Box(Modifier.size(84.dp).background(Color.White.copy(alpha = .8f), CircleShape), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Favorite, null, tint = Rose, modifier = Modifier.size(27.dp))
                            Text("9 / 40", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Plum)
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
                LinearProgressIndicator(progress = { 9f / 40f }, modifier = Modifier.fillMaxWidth().height(7.dp), color = Rose, trackColor = Color.White, strokeCap = androidx.compose.ui.graphics.StrokeCap.Round)
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CalendarMonth, null, tint = Rose, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(6.dp))
                    Text("Next check-up: add your appointment", color = Plum, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    TextButton(onClick = { onAction("Add appointment") }) { Text("Add", color = Rose) }
                }
            }
        }
        Spacer(Modifier.height(15.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MetricCard("Blood pressure", "— / —", "Add reading", Icons.Filled.FavoriteBorder, Blush, Modifier.weight(1f)) { onAction("Pregnancy record") }
            MetricCard("Baby profile", "Not linked", "Add details", Icons.Filled.ChildCare, Butter, Modifier.weight(1f)) { onAction("My Baby") }
        }
        Spacer(Modifier.height(18.dp))
        SectionTitle("Quick actions", "See all") { onAction("All care options") }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(11.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
            items(listOf(
                ActionItem("Emergency help", "Request referral", Icons.Filled.Emergency, Blush),
                ActionItem("Pregnancy record", "Visits & readings", Icons.Filled.Favorite, Color(0xFFF9DCCB)),
                ActionItem("My baby", "Newborn details", Icons.Filled.ChildCare, Butter),
                ActionItem("Health guide", "Simple guidance", Icons.Filled.MenuBook, PaleMint)
            )) { item -> QuickAction(item) { onAction(item.title) } }
        }
        Spacer(Modifier.height(12.dp))
        SectionTitle("Your pregnancy record", "View record") { onAction("Pregnancy record") }
        Surface(color = Color.White, shape = RoundedCornerShape(20.dp), shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(15.dp)) {
                RecordRow(Icons.Filled.Event, "Last menstrual period", "Not confirmed", Blush)
                HorizontalDivider(color = BorderSoft)
                RecordRow(Icons.Filled.MedicalServices, "Antenatal visits", "0 visits recorded", PaleMint)
                HorizontalDivider(color = BorderSoft)
                RecordRow(Icons.Filled.Medication, "Medicines / supplements", "Add with health worker", Butter)
            }
        }
        Spacer(Modifier.height(17.dp))
        SectionTitle("Coming up", "Manage") { onAction("Reminders") }
        ReminderRow("ANC check-up", "Add your next appointment", Icons.Filled.CalendarMonth, Blush) { onAction("Reminders") }
        ReminderRow("Weekly care tip", "Read a simple pregnancy tip", Icons.Filled.Lightbulb, Butter) { onAction("Health guide") }
        ReminderRow("Referral status", "No active referral in this demo", Icons.Filled.TrackChanges, PaleLilac) { onAction("Referral status") }
        Spacer(Modifier.height(90.dp))
    }
}

@Composable
private fun AshaHome(modifier: Modifier, onAction: (String) -> Unit) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(8.dp))
        Text("Good morning 👋", color = Muted, fontSize = 13.sp)
        Text("ASHA workspace", color = Plum, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Your community at a glance", color = Muted, fontSize = 13.sp, modifier = Modifier.padding(top = 3.dp, bottom = 15.dp))
        Surface(color = Color.White, shape = RoundedCornerShape(23.dp), shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.background(Brush.linearGradient(listOf(Color(0xFFFFE5EA), Color.White, Color(0xFFFFF0D1)))).padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) { Text("Sector overview", color = Muted, fontSize = 12.sp); Text("This week", color = Plum, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                    Icon(Icons.Filled.Groups, null, tint = Rose, modifier = Modifier.size(32.dp))
                }
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    MiniStat("Mothers", "24", Blush, Modifier.weight(1f)) { onAction("My Mothers") }
                    MiniStat("Newborns", "8", Butter, Modifier.weight(1f)) { onAction("Newborns") }
                    MiniStat("Visits due", "5", PaleMint, Modifier.weight(1f)) { onAction("Visits due") }
                }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Event, null, tint = Rose, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(6.dp))
                    Text("3 expected due dates this month", color = Plum, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    TextButton(onClick = { onAction("Due dates") }) { Text("View", color = Rose) }
                }
            }
        }
        Spacer(Modifier.height(17.dp))
        SectionTitle("Quick tools", "View all") { onAction("All tools") }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            QuickTile("Add patient", Icons.Filled.PersonAdd, Blush, Modifier.weight(1f)) { onAction("Add new patient") }
            QuickTile("Create referral", Icons.Filled.Assignment, Butter, Modifier.weight(1f)) { onAction("Create referral") }
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            QuickTile("Mother–child link", Icons.Filled.Link, PaleMint, Modifier.weight(1f)) { onAction("Mother & Baby Link") }
            QuickTile("Voice record", Icons.Filled.Mic, PaleLilac, Modifier.weight(1f)) { onAction("Speech to Record") }
        }
        Spacer(Modifier.height(18.dp))
        SectionTitle("Priority follow-ups", "See list") { onAction("Priority follow-ups") }
        ReminderRow("Visit due today", "2 mothers need a visit · demo data", Icons.Filled.NotificationsActive, Blush) { onAction("Visits due") }
        ReminderRow("Referral response pending", "1 request needs an update · demo data", Icons.Filled.TrackChanges, Butter) { onAction("Pending referrals") }
        ReminderRow("Newborn follow-up", "Review scheduled check-ins · demo data", Icons.Filled.ChildCare, PaleMint) { onAction("Newborn follow-up") }
        Spacer(Modifier.height(18.dp))
        SectionTitle("Recent patients", "View all") { onAction("My Mothers") }
        PatientRow("Ananya Patel", "Pregnancy · Week 9", "Visit due", Blush) { onAction("Ananya Patel") }
        PatientRow("Meera Shah", "Newborn · 12 days", "Follow-up", Butter) { onAction("Meera Shah") }
        Spacer(Modifier.height(90.dp))
    }
}

@Composable
private fun DriverHome(modifier: Modifier, onAction: (String) -> Unit) {
    var isAvailable by remember { mutableStateOf(true) }
    var tripFilter by remember { mutableStateOf("Upcoming") }

    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Good day, Driver 👋", color = Muted, fontSize = 13.sp)
                Text("Trip control", color = Plum, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Text("Your shift at a glance", color = Muted, fontSize = 13.sp)
            }
            Box(Modifier.size(54.dp).background(Butter, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.DirectionsCar, null, tint = Plum, modifier = Modifier.size(30.dp))
            }
        }
        Spacer(Modifier.height(17.dp))

        // Driver-specific availability console, visually distinct from mother and ASHA screens.
        Surface(color = Plum, shape = RoundedCornerShape(25.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(46.dp).background(Color.White.copy(alpha = .13f), RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.LocalShipping, null, tint = Butter, modifier = Modifier.size(27.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("SHIFT STATUS", color = Blush, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                        Text(if (isAvailable) "Available for trips" else "Off duty", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Vehicle ID · Demo AMB-04", color = Color.White.copy(alpha = .72f), fontSize = 12.sp)
                    }
                    Switch(checked = isAvailable, onCheckedChange = { isAvailable = it })
                }
                Spacer(Modifier.height(17.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    listOf(Triple("0", "New trips", Icons.Filled.NotificationsActive), Triple("0", "In progress", Icons.Filled.Route), Triple("0", "Completed", Icons.Filled.TaskAlt)).forEach { stat ->
                        Surface(color = Color.White.copy(alpha = .10f), shape = RoundedCornerShape(16.dp), modifier = Modifier.weight(1f).clickable { onAction(stat.second) }) {
                            Column(Modifier.padding(vertical = 12.dp, horizontal = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(stat.third, null, tint = Butter, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.height(5.dp))
                                Text(stat.first, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                                Text(stat.second, color = Color.White.copy(alpha = .8f), fontSize = 10.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Trip board", color = Plum, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("DEMO DATA", color = Rose, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(9.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Upcoming", "In progress", "History").forEach { filter ->
                val selected = tripFilter == filter
                Surface(
                    onClick = { tripFilter = filter },
                    color = if (selected) Rose else Color.White,
                    shape = RoundedCornerShape(50),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) Rose else BorderSoft)
                ) {
                    Text(filter, modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp), color = if (selected) Color.White else Plum, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        Surface(color = Color.White, shape = RoundedCornerShape(23.dp), shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(17.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(45.dp).background(Color(0xFFFFE5E8), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Route, null, tint = Rose, modifier = Modifier.size(25.dp))
                    }
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f)) {
                        Text(when (tripFilter) { "History" -> "Recent trip history"; "In progress" -> "Active journey"; else -> "No trip assigned yet" }, color = Plum, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(when (tripFilter) { "History" -> "Completed demo trips will appear here"; "In progress" -> "No active trip in demo mode"; else -> "Dispatch assignments will appear here" }, color = Muted, fontSize = 12.sp)
                    }
                    Icon(Icons.Filled.ChevronRight, null, tint = Rose)
                }
                Spacer(Modifier.height(15.dp))
                // Stylised route preview is intentionally illustrative, not live GPS.
                Surface(color = Color(0xFFFFF5E8), shape = RoundedCornerShape(17.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.RadioButtonChecked, null, tint = Rose, modifier = Modifier.size(19.dp))
                            Box(Modifier.width(2.dp).height(22.dp).background(Peach))
                            Icon(Icons.Filled.LocationOn, null, tint = Color(0xFF6D8E62), modifier = Modifier.size(21.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Pickup point", color = Muted, fontSize = 11.sp)
                            Text("Waiting for assignment", color = Plum, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(7.dp))
                            Text("Destination facility", color = Muted, fontSize = 11.sp)
                            Text("Shown after confirmed referral", color = Plum, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                Spacer(Modifier.height(13.dp))
                Button(
                    onClick = { onAction("Refresh trips") },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Rose)
                ) {
                    Icon(Icons.Filled.Refresh, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Refresh trip board", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Driver tools", color = Plum, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            QuickTile("Vehicle check", Icons.Filled.CarRepair, Butter, Modifier.weight(1f)) { onAction("Vehicle check") }
            QuickTile("Navigation", Icons.Filled.Navigation, PaleMint, Modifier.weight(1f)) { onAction("Navigation preview") }
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            QuickTile("Trip history", Icons.Filled.History, Blush, Modifier.weight(1f)) { onAction("Trip history") }
            QuickTile("Notifications", Icons.Filled.Notifications, PaleLilac, Modifier.weight(1f)) { onAction("Notifications") }
        }
        Spacer(Modifier.height(90.dp))
    }
}


@Composable
private fun HospitalHome(modifier: Modifier, onAction: (String) -> Unit) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(8.dp))
        Text("Referral coordination", color = Muted, fontSize = 13.sp)
        Text("Hospital workspace", color = Plum, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(15.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MiniStat("Incoming", "2", Blush, Modifier.weight(1f)) { onAction("Incoming referrals") }
            MiniStat("Awaiting", "1", Butter, Modifier.weight(1f)) { onAction("Pending response") }
            MiniStat("Handover", "0", PaleMint, Modifier.weight(1f)) { onAction("Clinical handover") }
        }
        Spacer(Modifier.height(18.dp))
        SectionTitle("Referral queue", "View all") { onAction("Incoming referrals") }
        PatientRow("Demo referral #MA-102", "Facility response pending", "Review", Blush) { onAction("Referral MA-102") }
        PatientRow("Demo referral #MA-101", "Awaiting transport update", "Open", Butter) { onAction("Referral MA-101") }
        Spacer(Modifier.height(15.dp))
        QuickTile("Facility readiness", Icons.Filled.LocalHospital, PaleMint, Modifier.fillMaxWidth()) { onAction("Facility readiness") }
        Spacer(Modifier.height(10.dp))
        QuickTile("Clinical handover", Icons.Filled.Handshake, PaleLilac, Modifier.fillMaxWidth()) { onAction("Clinical handover") }
        Spacer(Modifier.height(90.dp))
    }
}

@Composable
private fun SectionTitle(title: String, action: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(title, color = Plum, fontSize = 17.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        TextButton(onClick = onClick, contentPadding = PaddingValues(horizontal = 4.dp)) { Text(action, color = Rose, fontSize = 12.sp) }
    }
}

@Composable
private fun MetricCard(title: String, value: String, hint: String, icon: ImageVector, tint: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.White, shape = RoundedCornerShape(19.dp), shadowElevation = 1.dp, modifier = modifier) {
        Column(Modifier.padding(13.dp)) {
            Box(Modifier.size(36.dp).background(tint, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = Plum) }
            Spacer(Modifier.height(10.dp))
            Text(title, color = Muted, fontSize = 11.sp)
            Text(value, color = Plum, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(hint, color = Rose, fontSize = 11.sp)
        }
    }
}

@Composable
private fun QuickAction(item: ActionItem, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.White, shape = RoundedCornerShape(19.dp), shadowElevation = 1.dp, modifier = Modifier.width(132.dp)) {
        Column(Modifier.padding(13.dp)) {
            Box(Modifier.size(44.dp).background(item.tint, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) { Icon(item.icon, null, tint = Plum, modifier = Modifier.size(24.dp)) }
            Spacer(Modifier.height(11.dp))
            Text(item.title, color = Plum, fontWeight = FontWeight.Bold, fontSize = 13.sp, lineHeight = 16.sp)
            Text(item.subtitle, color = Muted, fontSize = 11.sp, lineHeight = 14.sp, modifier = Modifier.padding(top = 4.dp))
            Spacer(Modifier.height(5.dp))
            Icon(Icons.Filled.ArrowOutward, null, tint = Rose, modifier = Modifier.size(16.dp).align(Alignment.End))
        }
    }
}

@Composable
private fun QuickTile(title: String, icon: ImageVector, tint: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.White, shape = RoundedCornerShape(18.dp), shadowElevation = 1.dp, modifier = modifier) {
        Column(Modifier.padding(14.dp)) {
            Box(Modifier.size(42.dp).background(tint, RoundedCornerShape(13.dp)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = Plum, modifier = Modifier.size(23.dp)) }
            Spacer(Modifier.height(9.dp))
            Text(title, color = Plum, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("Tap to open", color = Muted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
        }
    }
}

@Composable
private fun MiniStat(title: String, value: String, tint: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.White, shape = RoundedCornerShape(16.dp), shadowElevation = 1.dp, modifier = modifier) {
        Column(Modifier.padding(11.dp)) {
            Box(Modifier.size(28.dp).background(tint, RoundedCornerShape(9.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Filled.Circle, null, tint = Plum, modifier = Modifier.size(10.dp)) }
            Spacer(Modifier.height(7.dp))
            Text(value, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold, color = Plum)
            Text(title, fontSize = 11.sp, color = Muted, lineHeight = 13.sp)
        }
    }
}

@Composable
private fun DriverStat(title: String, value: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.White.copy(alpha = .85f), shape = RoundedCornerShape(15.dp), modifier = modifier) {
        Column(Modifier.padding(10.dp)) {
            Icon(icon, null, tint = Rose, modifier = Modifier.size(20.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Plum)
            Text(title, fontSize = 10.sp, color = Muted)
        }
    }
}

@Composable
private fun RecordRow(icon: ImageVector, title: String, value: String, tint: Color) {
    Row(Modifier.fillMaxWidth().padding(vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(36.dp).background(tint, RoundedCornerShape(11.dp)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = Plum, modifier = Modifier.size(19.dp)) }
        Spacer(Modifier.width(11.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = Plum, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(value, color = Muted, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
        }
        Icon(Icons.Filled.ChevronRight, null, tint = Rose)
    }
}

@Composable
private fun ReminderRow(title: String, subtitle: String, icon: ImageVector, tint: Color, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.White, shape = RoundedCornerShape(17.dp), shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth().padding(bottom = 9.dp)) {
        Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(42.dp).background(tint, RoundedCornerShape(13.dp)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = Plum, modifier = Modifier.size(22.dp)) }
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) { Text(title, color = Plum, fontSize = 13.sp, fontWeight = FontWeight.Bold); Text(subtitle, color = Muted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp)) }
            Icon(Icons.Filled.ChevronRight, null, tint = Rose)
        }
    }
}

@Composable
private fun PatientRow(name: String, subtitle: String, status: String, tint: Color, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.White, shape = RoundedCornerShape(17.dp), shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth().padding(bottom = 9.dp)) {
        Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(43.dp).background(tint, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Filled.Person, null, tint = Plum, modifier = Modifier.size(23.dp)) }
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) { Text(name, color = Plum, fontSize = 13.sp, fontWeight = FontWeight.Bold); Text(subtitle, color = Muted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp)) }
            Surface(color = tint, shape = RoundedCornerShape(9.dp)) { Text(status, Modifier.padding(horizontal = 8.dp, vertical = 5.dp), fontSize = 10.sp, color = Plum, fontWeight = FontWeight.SemiBold) }
        }
    }
}
