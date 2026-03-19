package com.berkekucuk.mmaapp.core.presentation

val TrStrings = AppStrings(
    language = AppLanguage.TR,
    eventsTitle = "ETKİNLİKLER",
    tabUpcoming = "Yaklaşan",
    tabCompleted = "Tamamlanan",
    emptyUpcomingEvents = "Yaklaşan etkinlik bulunamadı",
    emptyEventsForYear = { "$it yılına ait etkinlik bulunamadı" },
    toBeAnnounced = "AÇIKLANACAK",
    selectYear = "Yıl Seçin",
    weightClassBout = { it },
    contentDescriptionFlag = "Bayrak",
    contentDescriptionWin = "Galibiyet",
    contentDescriptionLoss = "Yenilgi",
    unknownFighter = "Bilinmeyen Dövüşçü",
    eventDetailsFallback = "Etkinlik Detayları",
    contentDescriptionBack = "Geri",
    tabMainCard = "Ana Kart",
    tabPrelims = "Ön Maçlar",
    emptyMainCardFights = "Mevcut değil",
    emptyPrelimFights = "Mevcut değil",
    fightDetailLabelName = "İsim",
    fightDetailLabelAge = "Maçtaki Yaş",
    fightDetailLabelHometown = "Temsil Ediyor",
    fightDetailLabelHeight = "Boy",
    fightDetailLabelReach = "Erişim Mesafesi",
    fightDetailLabelResult = "Sonuç",
    fightDetailLabelOdds = "Oranlar",
    fightDetailLabelRecord = "Maş Sonrası Rekoru",
    fightDetailLabelRoundsFormat = "Tur Formatı",
    fightDetailLabelMethod = "Yöntem",
    fightDetailLabelRoundSummary = "Tur Özeti",
    heightCm = { "$it cm" },
    fightResultDefeats = { winner, loser -> "$winner, $loser'ı yendi" },
    fightResultDraw = "Beraberlik",
    fightResultNoContest = "İptal",
    fightResultVia = { "$it ile" },
    rankingsTitle = "Sıralamalar",
    tabMens = "Erkekler",
    tabWomens = "Kadınlar",
    rankingsChampion = "ŞAMPİYON",
    rankingsVacant = "Boş",
    contentDescriptionCollapse = "Kapat",
    contentDescriptionExpand = "Aç",
    rankingsChampionRankLabel = "C",
    tabOverview = "Genel",
    tabFights = "Maçlar",
    fighterDetailLabelRecord = "Rekor",
    fighterDetailLabelWeightClass = "Siklet",
    fighterDetailLabelHeight = "Boy",
    fighterDetailLabelReach = "Erişim Mesafesi",
    fighterDetailLabelAge = "Yaş",
    fighterDetailLabelDateOfBirth = "Doğum Tarihi",
    fighterDetailLabelBorn = "Doğum Yeri",
    fighterDetailLabelFightingOutOf = "Temsil Ediyor",
    fighterDetailValueUnavailable = "—",
    fighterDetailAgeYears = { age, years -> "$age ($years yaş)" },
    fighterDetailRecordWins = "Galibiyet",
    fighterDetailRecordLosses = "Yenilgi",
    fighterDetailRecordDraws = "Beraberlik",
    fighterDetailResultWin = "W",
    fighterDetailResultLoss = "L",
    fighterDetailResultDraw = "D",
    fighterDetailResultNoContest = "NC",
    fighterDetailResultPending = "–",
    loginTitleMma = "MMA",
    loginTitleApp = "APP",
    loginSubtitle = "Dövüşler · Etkinlikler · Sıralamalar",
    loginSignInGoogle = "Google ile Giriş Yap",
    loginSecureSignIn = "güvenli giriş",
    contentDescriptionGoogle = "Google",
    profileEdit = "Düzenle",
    profileSignOut = "Çıkış Yap",
    profileTabOverview = "Genel",
    profileTabPredictions = "Tahminler",
    profileEditTitle = "Profili Düzenle",
    profileEditPersonalInfo = "Kişisel Bilgiler",
    profileEditFullName = "Ad Soyad",
    profileEditUsernameLabel = "Kullanıcı Adı",
    profileEditSaveChanges = "Değişiklikleri Kaydet",
    profileEditErrorNetwork = "Bağlantınızı kontrol edip tekrar deneyin.",
    profileEditErrorUsernameTaken = "Bu kullanıcı adı zaten alınmış.",
    profileEditErrorEmptyUsername = "Kullanıcı adı boş olamaz.",
    profileEditErrorInvalidUsername = "Kullanıcı adı yalnızca a-z, 0-9, _ ve nokta içerebilir.",
    profileEditErrorUsernameShort = "Kullanıcı adı en az 3 karakter olmalıdır.",
    profileEditErrorUsernameLong = "Kullanıcı adı en fazla 20 karakter olmalıdır.",
    profileEditErrorEmptyFullname = "Ad soyad boş olamaz.",
    profileEditErrorFullnameShort = "Ad soyad en az 3 karakter olmalıdır.",
    profileEditErrorFullnameLong = "Ad soyad en fazla 50 karakter olmalıdır.",
    profileEditErrorUnknown = "Bilinmeyen bir hata oluştu.",
    navFights = "Dövüşler",
    navRankings = "Sıralamalar",
    navProfile = "Profil",
    weightClassDisplayName = { name ->
        when (name) {
            "STRAWWEIGHT" -> "Saman Siklet"
            "FLYWEIGHT" -> "Sinek Siklet"
            "WOMENS_FLYWEIGHT" -> "Kadınlar Sinek Siklet"
            "BANTAMWEIGHT" -> "Horoz Siklet"
            "WOMENS_BANTAMWEIGHT" -> "Kadınlar Horoz Siklet"
            "FEATHERWEIGHT" -> "Tüy Siklet"
            "WOMENS_FEATHERWEIGHT" -> "Kadınlar Tüy Siklet"
            "LIGHTWEIGHT" -> "Hafif Siklet"
            "WELTERWEIGHT" -> "Welter Siklet"
            "MIDDLEWEIGHT" -> "Orta Siklet"
            "LIGHTHEAVYWEIGHT" -> "Hafif Ağır Siklet"
            "HEAVYWEIGHT" -> "Ağır Siklet"
            "CATCHWEIGHT" -> "Ara Siklet"
            else -> name.replace("_", " ").lowercase().split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
        }
    },
    resultDisplayName = { name ->
        when (name) {
            "WIN" -> "Galibiyet"
            "LOSS" -> "Yenilgi"
            "DRAW" -> "Beraberlik"
            "NO_CONTEST" -> "Sonuçsuz"
            "PENDING" -> "Beklemede"
            "CANCELLED" -> "İptal Edildi"
            "FIZZLED" -> "Gerçekleşmedi"
            else -> name.lowercase().replaceFirstChar { it.uppercase() }
        }
    },
)