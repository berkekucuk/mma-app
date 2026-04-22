package com.berkekucuk.mmaapp.core.presentation.strings

import com.berkekucuk.mmaapp.core.presentation.AppLanguage

val EnStrings = AppStrings(
    language = AppLanguage.EN,
    eventsTitle = "EVENTS",
    tabUpcoming = "Upcoming",
    tabCompleted = "Completed",
    toBeAnnounced = "TO BE ANNOUNCED",
    tba = "tba",
    liveEvent = "Live Event",
    selectYear = "Select Year",
    weightClassBout = { "$it Bout" },
    contentDescriptionFlag = "Flag",
    contentDescriptionWin = "Win",
    contentDescriptionLoss = "Loss",
    unknownFighter = "Unknown Fighter",
    eventDetailsFallback = "Event Details",
    contentDescriptionBack = "Back",
    tabMainCard = "Main Card",
    tabPrelims = "Prelims",
    emptyMainCardFights = "No main card fights available",
    emptyPrelimFights = "No prelim fights available",
    tabFightDetails = "Details",
    tabFightComparison = "Comparison",
    radarLabelHeight = "Height",
    radarLabelReach = "Reach",
    radarLabelOdds = "Odds",
    radarLabelWinRate = "Win %",
    radarLabelKoTkoRate = "KO/TKO %",
    radarLabelSubRate = "Submission %",
    fightDetailLabelName = "Name",
    fightDetailLabelAge = "Age at Fight",
    fightDetailLabelHometown = "Fighting out of",
    fightDetailLabelHeight = "Height",
    fightDetailLabelReach = "Reach",
    fightDetailLabelResult = "Result",
    fightDetailLabelOdds = "Odds",
    fightDetailLabelRecord = "Record After Fight",
    fightDetailLabelRoundsFormat = "Format",
    fightDetailLabelMethod = "Method",
    fightDetailLabelRoundSummary = "Time",
    heightCm = { "$it cm" },
    fightResultDefeats = { winner, loser -> "$winner defeats $loser" },
    fightResultDraw = "Draw",
    fightResultNoContest = "No Contest",
    fightResultVia = { "via $it" },
    rankingsTitle = "Rankings",
    tabMens = "Men's",
    tabWomens = "Women's",
    rankingsChampion = "CHAMPION",
    rankingsVacant = "Vacant",
    contentDescriptionCollapse = "Collapse",
    contentDescriptionExpand = "Expand",
    rankingsChampionRankLabel = "C",
    tabOverview = "Overview",
    tabFights = "Fights",
    fighterDetailLabelRecord = "Record",
    fighterDetailLabelWeightClass = "Weight Class",
    fighterDetailLabelHeight = "Height",
    fighterDetailLabelReach = "Reach",
    fighterDetailLabelAge = "Age",
    fighterDetailLabelDateOfBirth = "Date of Birth",
    fighterDetailLabelBorn = "Born",
    fighterDetailLabelFightingOutOf = "Fighting Out Of",
    fighterDetailValueUnavailable = "—",
    fighterDetailAgeYears = { age, years -> "$age ($years yrs)" },
    fighterDetailRecordWins = "Wins",
    fighterDetailRecordLosses = "Losses",
    fighterDetailRecordDraws = "Draws",
    fighterDetailResultWin = "W",
    fighterDetailResultLoss = "L",
    fighterDetailResultDraw = "D",
    fighterDetailResultNoContest = "NC",
    fighterDetailResultPending = "–",
    loginTitleMma = "MMA",
    loginTitleApp = "APP",
    loginSubtitle = "Fights · Events · Rankings",
    loginSignInGoogle = "Sign in with Google",
    loginSecureSignIn = "secure sign in",
    contentDescriptionGoogle = "Google",
    profileEdit = "Edit",
    profileSignOut = "Sign Out",
    profileTabOverview = "Overview",
    profileTabPredictions = "Predictions",
    profileFavoriteFighters = "Favorite Fighters",
    profileEditTitle = "Edit Profile",
    profileEditPersonalInfo = "Personal Information",
    profileEditFullName = "Full Name",
    profileEditUsernameLabel = "Username",
    profileEditSaveChanges = "Save Changes",
    profileEditErrorUsernameTaken = "This username is already taken.",
    profileEditErrorEmptyUsername = "Username cannot be empty.",
    profileEditErrorInvalidUsername = "Username can only contain a-z, 0-9, _, and dots.",
    profileEditErrorUsernameShort = "Username must be at least 3 characters.",
    profileEditErrorUsernameLong = "Username must be at most 20 characters.",
    profileEditErrorEmptyFullname = "Full name cannot be empty.",
    profileEditErrorFullnameShort = "Full name must be at least 3 characters.",
    profileEditErrorFullnameLong = "Full name must be at most 50 characters.",
    fighterSearchPlaceholder = "Search fighters...",
    fighterSearchEmpty = "No fighters found",
    errorNetwork = "No internet connection. Please try again.",
    errorNetwork2 = "No internet connection.",
    errorUnknown = "Something went wrong. Please try again.",
    errorPleaseSignIn = "Please sign in to use this feature.",
    errorFightCompleted = "This fight is already over.",
    retry = "Retry",
    dialogAccept = "Accept",
    dialogCancel = "Cancel",
    fightNotificationDialogMessage = "You'll receive notifications before the fight starts and for the result.",
    fightNotificationRemoveDialogMessage = "Turn off notifications for this fight.",
    settingsTitle = "Settings",
    settingsSectionLanguage = "Language",
    settingsSectionMeasurements = "Measurements",
    settingsSectionOdds = "Odds Format",
    settingsSectionTheme = "Theme",
    settingsThemeLight = "Light Mode",
    settingsThemeDark = "Dark Mode",
    menuSignInPrompt = "Sign in to follow your favorite fighters and get real-time event notifications.",
    menuTitle = "Menu",
    menuItemLeaderboard = "Leaderboard",
    menuItemNotifications = "Notifications",
    menuItemSettings = "Settings",
    menuSignInButton = "Sign in",
    menuSignInTerms = "By signing in you accept the Terms of Use and Privacy Policy",
    menuProfileSettings = "Profile Settings",
    menuSignInWithGoogle = "Sign in with Google",
    menuNotificationsDisabled = "You have disabled Notifications",
    menuNotificationsEnabled = "Enabled",
    navEvents = "Events",
    navRankings = "Rankings",
    navMenu = "Menu",
    weightClassDisplayName = { id ->
        when (id.uppercase()) {
            "SW" -> "Women's Strawweight"
            "FLW" -> "Flyweight"
            "W_FLW" -> "Women's Flyweight"
            "BW" -> "Bantamweight"
            "W_BW" -> "Women's Bantamweight"
            "FW" -> "Featherweight"
            "W_FW" -> "Women's Featherweight"
            "LW" -> "Lightweight"
            "WW" -> "Welterweight"
            "MW" -> "Middleweight"
            "LHW" -> "Light Heavyweight"
            "HW" -> "Heavyweight"
            "CW" -> "Catchweight"
            "MENS_P4P" -> "Men's Pound-for-Pound"
            "WOMENS_P4P" -> "Women's Pound-for-Pound"
            else -> id
        }
    },
    resultDisplayName = { name ->
        when (name) {
            "WIN" -> "Win"
            "LOSS" -> "Loss"
            "DRAW" -> "Draw"
            "NO_CONTEST" -> "No Contest"
            "PENDING" -> "Pending"
            "CANCELLED" -> "Cancelled"
            "FIZZLED" -> "Fizzled"
            else -> name.lowercase().replaceFirstChar { it.uppercase() }
        }
    },
    toUpperCase = { it.uppercase() },
)