package com.berkekucuk.mmaapp.core.presentation.strings

import com.berkekucuk.mmaapp.core.presentation.AppLanguage
import com.berkekucuk.mmaapp.core.utils.AppError

val EnStrings = AppStrings(
    language = AppLanguage.EN,
    tabUpcoming = "Upcoming",
    tabCompleted = "Completed",
    toBeAnnounced = "TO BE ANNOUNCED",
    tba = "tba",
    liveEvent = "Live Event",
    selectYear = "Select Year",
    contentDescriptionFlag = "Flag",
    contentDescriptionInfo = "Information",
    contentDescriptionWin = "Win",
    contentDescriptionLoss = "Loss",
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
    radarLabelKoTkoRate = "KO/TKO %\n(UFC)",
    radarLabelSubRate = "Submission %\n(UFC)",
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
    profileEdit = "Edit",
    profileSignOut = "Sign Out",
    profileTabOverview = "Overview",
    profileTabPredictions = "Predictions",
    profileFavoriteFighters = "Favorite Fighters",
    profileGoatFighters = "Goat List",
    profileHatedFighters = "Hated Fighters",
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
    predictionQuestionTitle = "Who will win?",
    errorOddsNotPublished = "Odds pending. Predictions opening soon.",
    errorEventOver = "Event already over.",
    errorFightOver = "This fight is already over.",
    errorFightPending = "Result pending. Predictions locked.",
    leaderboardInfoTitle = "Points System",
    leaderboardInfoText = "Points are based on the reward value of the odds at the time of your prediction.\n" +
            "\n" + "The 100-Point Rule: Each 1.00 unit of net profit in decimal odds equals 100 points.\n" +
            "\n" + "High Odds: A 3.00 odd offers 2.00 units of profit, resulting in 200 points.\n" +
            "\n" + "Low Odds: A 1.25 odd offers 0.25 units of profit, resulting in 25 points.\n" +
            "\n" + "Lock-in: Your potential reward is fixed the moment you lock your prediction.\n" +
            "\n" + "Result: Points are only awarded for correct predictions.",
    leaderboardInfoClose = "Close",
    notificationChannelName = "Fight Results",
    notificationChannelDescription = "Get notified about fight results",
    commonRemove = "Remove",
    commonCancel = "Cancel",
    profileRemoveFighterConfirm = { name -> "Remove $name from your list?" },
    mapError = { error ->
        when (error) {
            AppError.NETWORK -> "No internet connection. Please try again."
            AppError.SERVER_ERROR -> "Something went wrong. Please try again."
            else -> null
        }
    }
)
