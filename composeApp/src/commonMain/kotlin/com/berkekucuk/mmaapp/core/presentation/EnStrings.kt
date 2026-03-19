package com.berkekucuk.mmaapp.core.presentation

val EnStrings = AppStrings(
    language = AppLanguage.EN,
    eventsTitle = "EVENTS",
    tabUpcoming = "Upcoming",
    tabCompleted = "Completed",
    emptyUpcomingEvents = "No upcoming events available",
    emptyEventsForYear = { "No events available for $it" },
    toBeAnnounced = "TO BE ANNOUNCED",
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
    fightDetailLabelName = "Name",
    fightDetailLabelAge = "Age at Fight",
    fightDetailLabelHometown = "Fighting out of",
    fightDetailLabelHeight = "Height",
    fightDetailLabelReach = "Reach",
    fightDetailLabelResult = "Result",
    fightDetailLabelOdds = "Odds",
    fightDetailLabelRecord = "Record After Fight",
    fightDetailLabelRoundsFormat = "Rounds Format",
    fightDetailLabelMethod = "Method",
    fightDetailLabelRoundSummary = "Round Summary",
    heightCm = { "$it cm" },
    fightResultDefeats = { winner, loser -> "$winner defeats $loser" },
    fightResultDraw = "Draw",
    fightResultNoContest = "No Contest",
    fightResultVia = { "via $it" },
    rankingsTitle = "rankings",
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
    profileEditTitle = "Edit Profile",
    profileEditPersonalInfo = "Personal Information",
    profileEditFullName = "Full Name",
    profileEditUsernameLabel = "Username",
    profileEditSaveChanges = "Save Changes",
    profileEditErrorNetwork = "Please check your connection and try again.",
    profileEditErrorUsernameTaken = "This username is already taken.",
    profileEditErrorEmptyUsername = "Username cannot be empty.",
    profileEditErrorInvalidUsername = "Username can only contain a-z, 0-9, _, and dots.",
    profileEditErrorUsernameShort = "Username must be at least 3 characters.",
    profileEditErrorUsernameLong = "Username must be at most 20 characters.",
    profileEditErrorEmptyFullname = "Full name cannot be empty.",
    profileEditErrorFullnameShort = "Full name must be at least 3 characters.",
    profileEditErrorFullnameLong = "Full name must be at most 50 characters.",
    profileEditErrorUnknown = "An unknown error occurred.",
    navFights = "Fights",
    navRankings = "Rankings",
    navProfile = "Profile",
    weightClassDisplayName = { name ->
        when (name) {
            "STRAWWEIGHT" -> "Strawweight"
            "FLYWEIGHT" -> "Flyweight"
            "WOMENS_FLYWEIGHT" -> "Women's Flyweight"
            "BANTAMWEIGHT" -> "Bantamweight"
            "WOMENS_BANTAMWEIGHT" -> "Women's Bantamweight"
            "FEATHERWEIGHT" -> "Featherweight"
            "WOMENS_FEATHERWEIGHT" -> "Women's Featherweight"
            "LIGHTWEIGHT" -> "Lightweight"
            "WELTERWEIGHT" -> "Welterweight"
            "MIDDLEWEIGHT" -> "Middleweight"
            "LIGHTHEAVYWEIGHT" -> "Light Heavyweight"
            "HEAVYWEIGHT" -> "Heavyweight"
            "CATCHWEIGHT" -> "Catchweight"
            else -> name.replace("_", " ").lowercase().split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
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
)