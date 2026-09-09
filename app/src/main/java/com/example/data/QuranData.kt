package com.example.data

data class SurahInfo(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameTranslation: String,
    val revelationType: String, // "Meccan" or "Medinan"
    val totalVerses: Int,
    val startPage: Int
)

data class ParaInfo(
    val number: Int,
    val nameArabic: String,
    val nameRoman: String,
    val startPage: Int,
    val endPage: Int
)

data class NameOfAllah(
    val number: Int,
    val arabic: String,
    val transliteration: String,
    val meaning: String
)

data class TajweedRule(
    val title: String,
    val category: String,
    val description: String,
    val example: String
)

object QuranData {

    val surahs: List<SurahInfo> = listOf(
        SurahInfo(1, "الفاتحة", "Al-Fatihah", "The Opener", "Meccan", 7, 1),
        SurahInfo(2, "البقرة", "Al-Baqarah", "The Cow", "Medinan", 286, 2),
        SurahInfo(3, "آل عمران", "Ali 'Imran", "Family of Imran", "Medinan", 200, 50),
        SurahInfo(4, "النساء", "An-Nisa", "The Women", "Medinan", 176, 77),
        SurahInfo(5, "المائدة", "Al-Ma'idah", "The Table Spread", "Medinan", 120, 106),
        SurahInfo(6, "الأنعام", "Al-An'am", "The Cattle", "Meccan", 165, 128),
        SurahInfo(7, "الأعراف", "Al-A'raf", "The Heights", "Meccan", 206, 151),
        SurahInfo(8, "الأنفال", "Al-Anfal", "The Spoils of War", "Medinan", 75, 177),
        SurahInfo(9, "التوبة", "At-Tawbah", "The Repentance", "Medinan", 129, 187),
        SurahInfo(10, "يونس", "Yunus", "Jonah", "Meccan", 109, 208),
        SurahInfo(11, "هود", "Hud", "Hud", "Meccan", 123, 221),
        SurahInfo(12, "يوسف", "Yusuf", "Joseph", "Meccan", 111, 235),
        SurahInfo(13, "الرعد", "Ar-Ra'd", "The Thunder", "Medinan", 43, 249),
        SurahInfo(14, "إبراهيم", "Ibrahim", "Abraham", "Meccan", 52, 255),
        SurahInfo(15, "الحجر", "Al-Hijr", "The Rocky Tract", "Meccan", 99, 262),
        SurahInfo(16, "النحل", "An-Nahl", "The Bee", "Meccan", 128, 267),
        SurahInfo(17, "الإسراء", "Al-Isra", "The Night Journey", "Meccan", 111, 282),
        SurahInfo(18, "الكهف", "Al-Kahf", "The Cave", "Meccan", 110, 293),
        SurahInfo(19, "مريم", "Maryam", "Mary", "Meccan", 98, 305),
        SurahInfo(20, "طه", "Ta-Ha", "Ta-Ha", "Meccan", 135, 312),
        SurahInfo(21, "الأنبياء", "Al-Anbiya", "The Prophets", "Meccan", 112, 322),
        SurahInfo(22, "الحج", "Al-Hajj", "The Pilgrimage", "Medinan", 78, 332),
        SurahInfo(23, "المؤمنون", "Al-Mu'minun", "The Believers", "Meccan", 118, 342),
        SurahInfo(24, "النور", "An-Nur", "The Light", "Medinan", 64, 350),
        SurahInfo(25, "الفرقان", "Al-Furqan", "The Criterion", "Meccan", 77, 359),
        SurahInfo(26, "الشعراء", "Ash-Shu'ara", "The Poets", "Meccan", 227, 367),
        SurahInfo(27, "النمل", "An-Naml", "The Ant", "Meccan", 93, 377),
        SurahInfo(28, "القصص", "Al-Qasas", "The Stories", "Meccan", 88, 385),
        SurahInfo(29, "العنكبوت", "Al-'Ankabut", "The Spider", "Meccan", 69, 396),
        SurahInfo(30, "الروم", "Ar-Rum", "The Romans", "Meccan", 60, 404),
        SurahInfo(31, "لقمان", "Luqman", "Luqman", "Meccan", 34, 411),
        SurahInfo(32, "السجدة", "As-Sajdah", "The Prostration", "Meccan", 30, 415),
        SurahInfo(33, "الأحزاب", "Al-Ahzab", "The Combined Forces", "Medinan", 73, 418),
        SurahInfo(34, "سبإ", "Saba", "Sheba", "Meccan", 54, 428),
        SurahInfo(35, "فاطر", "Fatir", "Originator", "Meccan", 45, 434),
        SurahInfo(36, "يس", "Ya-Sin", "Ya-Sin", "Meccan", 83, 440),
        SurahInfo(37, "الصافات", "As-Saffat", "Those who set the Ranks", "Meccan", 182, 446),
        SurahInfo(38, "ص", "Sad", "The Letter Sad", "Meccan", 88, 453),
        SurahInfo(39, "الزمر", "Az-Zumar", "The Troops", "Meccan", 75, 458),
        SurahInfo(40, "غافر", "Ghafir", "The Forgiver", "Meccan", 85, 467),
        SurahInfo(41, "فصلت", "Fussilat", "Explained in Detail", "Meccan", 54, 477),
        SurahInfo(42, "الشورى", "Ash-Shura", "The Consultation", "Meccan", 53, 483),
        SurahInfo(43, "الزخرف", "Az-Zukhruf", "The Ornaments of Gold", "Meccan", 89, 489),
        SurahInfo(44, "الدخان", "Ad-Dukhan", "The Smoke", "Meccan", 59, 496),
        SurahInfo(45, "الجاثية", "Al-Jathiyah", "The Crouching", "Meccan", 37, 499),
        SurahInfo(46, "الأحقاف", "Al-Ahqaf", "The Wind-Curved Sandhills", "Meccan", 35, 502),
        SurahInfo(47, "محمد", "Muhammad", "Muhammad", "Medinan", 38, 507),
        SurahInfo(48, "الفتح", "Al-Fath", "The Victory", "Medinan", 29, 511),
        SurahInfo(49, "الحجرات", "Al-Hujurat", "The Rooms", "Medinan", 18, 515),
        SurahInfo(50, "ق", "Qaf", "The Letter Qaf", "Meccan", 45, 518),
        SurahInfo(51, "الذاريات", "Adh-Dhariyat", "The Winnowing Winds", "Meccan", 60, 520),
        SurahInfo(52, "الطور", "At-Tur", "The Mount", "Meccan", 49, 523),
        SurahInfo(53, "النجم", "An-Najm", "The Star", "Meccan", 62, 526),
        SurahInfo(54, "القمر", "Al-Qamar", "The Moon", "Meccan", 55, 528),
        SurahInfo(55, "الرحمن", "Ar-Rahman", "The Beneficent", "Medinan", 78, 531),
        SurahInfo(56, "الواقعة", "Al-Waqi'ah", "The Inevitable", "Meccan", 96, 534),
        SurahInfo(57, "الحديد", "Al-Hadid", "The Iron", "Medinan", 29, 537),
        SurahInfo(58, "المجادلة", "Al-Mujadila", "The Pleading Woman", "Medinan", 22, 542),
        SurahInfo(59, "الحشر", "Al-Hashr", "The Exile", "Medinan", 24, 545),
        SurahInfo(60, "الممتحنة", "Al-Mumtahanah", "She that is to be examined", "Medinan", 13, 549),
        SurahInfo(61, "الصف", "As-Saff", "The Ranks", "Medinan", 14, 551),
        SurahInfo(62, "الجمعة", "Al-Jumu'ah", "Friday", "Medinan", 11, 553),
        SurahInfo(63, "المنافقون", "Al-Munafiqun", "The Hypocrites", "Medinan", 11, 554),
        SurahInfo(64, "التغابن", "At-Taghabun", "Mutual Disillusion", "Medinan", 18, 556),
        SurahInfo(65, "الطلاق", "At-Talaq", "The Divorce", "Medinan", 12, 558),
        SurahInfo(66, "التحريم", "At-Tahrim", "The Prohibition", "Medinan", 12, 560),
        SurahInfo(67, "الملك", "Al-Mulk", "The Sovereignty", "Meccan", 30, 562),
        SurahInfo(68, "القلم", "Al-Qalam", "The Pen", "Meccan", 52, 564),
        SurahInfo(69, "الحاقة", "Al-Haqqah", "The Reality", "Meccan", 52, 566),
        SurahInfo(70, "المعارج", "Al-Ma'arij", "The Ascending Stairways", "Meccan", 44, 568),
        SurahInfo(71, "نوح", "Nuh", "Noah", "Meccan", 28, 570),
        SurahInfo(72, "الجن", "Al-Jinn", "The Jinn", "Meccan", 28, 572),
        SurahInfo(73, "المزمل", "Al-Muzzammil", "The Enshrouded One", "Meccan", 20, 574),
        SurahInfo(74, "المدثر", "Al-Muddaththir", "The Cloaked One", "Meccan", 56, 575),
        SurahInfo(75, "القيامة", "Al-Qiyamah", "The Resurrection", "Meccan", 40, 577),
        SurahInfo(76, "الإنسان", "Al-Insan", "Man", "Medinan", 31, 578),
        SurahInfo(77, "المرسلات", "Al-Mursalat", "The Emissaries", "Meccan", 50, 580),
        SurahInfo(78, "النبإ", "An-Naba", "The Tidings", "Meccan", 40, 582),
        SurahInfo(79, "النازعات", "An-Nazi'at", "Those who drag forth", "Meccan", 46, 583),
        SurahInfo(80, "عبس", "'Abasa", "He Frowned", "Meccan", 42, 585),
        SurahInfo(81, "التكوير", "At-Takwir", "The Overthrowing", "Meccan", 29, 586),
        SurahInfo(82, "الانفطار", "Al-Infitar", "The Cleaving", "Meccan", 19, 587),
        SurahInfo(83, "المطففين", "Al-Mutaffifin", "Defrauding", "Meccan", 36, 587),
        SurahInfo(84, "الانشقاق", "Al-Inshiqaq", "The Splitting Open", "Meccan", 25, 589),
        SurahInfo(85, "البروج", "Al-Buruj", "The Mansions of the Stars", "Meccan", 22, 590),
        SurahInfo(86, "الطارق", "At-Tariq", "The Morning Star", "Meccan", 17, 591),
        SurahInfo(87, "الأعلى", "Al-A'la", "The Most High", "Meccan", 19, 591),
        SurahInfo(88, "الغاشية", "Al-Ghashiyah", "The Overwhelming", "Meccan", 26, 592),
        SurahInfo(89, "الفجر", "Al-Fajr", "The Dawn", "Meccan", 30, 593),
        SurahInfo(90, "البلد", "Al-Balad", "The City", "Meccan", 20, 594),
        SurahInfo(91, "الشمس", "Ash-Shams", "The Sun", "Meccan", 15, 595),
        SurahInfo(92, "الليل", "Al-Layl", "The Night", "Meccan", 21, 595),
        SurahInfo(93, "الضحى", "Ad-Duha", "The Morning Hours", "Meccan", 11, 596),
        SurahInfo(94, "الشرح", "Ash-Sharh", "The Relief", "Meccan", 8, 596),
        SurahInfo(95, "التين", "At-Tin", "The Fig", "Meccan", 8, 597),
        SurahInfo(96, "العلق", "Al-'Alaq", "The Clot", "Meccan", 19, 597),
        SurahInfo(97, "القدر", "Al-Qadr", "The Power", "Meccan", 5, 598),
        SurahInfo(98, "البينة", "Al-Bayyinah", "The Clear Proof", "Medinan", 8, 598),
        SurahInfo(99, "الزلزلة", "Az-Zalzalah", "The Earthquake", "Medinan", 8, 599),
        SurahInfo(100, "العاديات", "Al-'Adiyat", "The Courser", "Meccan", 11, 599),
        SurahInfo(101, "القارعة", "Al-Qari'ah", "The Calamity", "Meccan", 11, 600),
        SurahInfo(102, "التكاثر", "At-Takathur", "The Rivalry in world increase", "Meccan", 8, 600),
        SurahInfo(103, "العصر", "Al-'Asr", "The Declining Day", "Meccan", 3, 601),
        SurahInfo(104, "الهمزة", "Al-Humazah", "The Traducer", "Meccan", 9, 601),
        SurahInfo(105, "الفيل", "Al-Fil", "The Elephant", "Meccan", 5, 601),
        SurahInfo(106, "قريش", "Quraysh", "Quraysh", "Meccan", 4, 602),
        SurahInfo(107, "الماعون", "Al-Ma'un", "The Small kindnesses", "Meccan", 7, 602),
        SurahInfo(108, "الكوثر", "Al-Kawthar", "The Abundance", "Meccan", 3, 602),
        SurahInfo(109, "الكافرون", "Al-Kafirun", "The Disbelievers", "Meccan", 6, 603),
        SurahInfo(110, "النصر", "An-Nasr", "The Divine Support", "Medinan", 3, 603),
        SurahInfo(111, "المسد", "Al-Masad", "The Palm Fiber", "Meccan", 5, 603),
        SurahInfo(112, "الإخلاص", "Al-Ikhlas", "The Sincerity", "Meccan", 4, 604),
        SurahInfo(113, "الفلق", "Al-Falaq", "The Daybreak", "Meccan", 5, 604),
        SurahInfo(114, "الناس", "An-Nas", "Mankind", "Meccan", 6, 604)
    )

    val paras: List<ParaInfo> = listOf(
        ParaInfo(1, "الم", "Alif Lam Meem", 1, 21),
        ParaInfo(2, "سيقول", "Sayaqool", 22, 41),
        ParaInfo(3, "تلك الرسل", "Tilka-r-Rusul", 42, 61),
        ParaInfo(4, "لن تنالوا", "Lan Tanalu", 62, 81),
        ParaInfo(5, "والمحصنات", "Wal Muhsanat", 82, 101),
        ParaInfo(6, "لا يحب الله", "La Yuhibbullah", 102, 120),
        ParaInfo(7, "وإذا سمعوا", "Wa Iza Samiu", 121, 141),
        ParaInfo(8, "ولو أننا", "Wa Lau Annana", 142, 161),
        ParaInfo(9, "قال الملأ", "Qalal Mala'u", 162, 181),
        ParaInfo(10, "واعلموا", "Wa'lamu", 182, 200),
        ParaInfo(11, "يعتذرون", "Ya'tadhirun", 201, 221),
        ParaInfo(12, "وما من دابة", "Wa Ma Min Dabbah", 222, 241),
        ParaInfo(13, "وما أبرئ", "Wa Ma Ubri'u", 242, 261),
        ParaInfo(14, "ربما", "Rubama", 262, 281),
        ParaInfo(15, "سبحان الذي", "Subhanallazi", 282, 301),
        ParaInfo(16, "قال ألم", "Qala Alam", 302, 321),
        ParaInfo(17, "اقترب", "Iqtaraba", 322, 341),
        ParaInfo(18, "قد أفلح", "Qad Aflaha", 342, 361),
        ParaInfo(19, "وقال الذين", "Wa Qalallazina", 362, 381),
        ParaInfo(20, "أمن خلق", "Amman Khalaq", 382, 401),
        ParaInfo(21, "اتل ما أوحي", "Utlu Ma Oohiya", 402, 421),
        ParaInfo(22, "ومن يقنت", "Wa Man Yaqnut", 422, 441),
        ParaInfo(23, "وما لي", "Wa Maliya", 442, 461),
        ParaInfo(24, "فمن أظلم", "Faman Azlamu", 462, 481),
        ParaInfo(25, "إليه يرد", "Ilayhi Yuraddu", 482, 501),
        ParaInfo(26, "حم", "Ha-Meem", 502, 521),
        ParaInfo(27, "قال فما خطبكم", "Qala Fama Khatbukum", 522, 541),
        ParaInfo(28, "قد سمع الله", "Qad Sami'Allah", 542, 561),
        ParaInfo(29, "تبارك الذي", "Tabarakallazi", 562, 581),
        ParaInfo(30, "عم يتساءلون", "Amma Yatasa'alun", 582, 604)
    )

    fun getSurahForPage(page: Int): SurahInfo {
        val clampedPage = page.coerceIn(1, 604)
        for (i in surahs.indices.reversed()) {
            if (clampedPage >= surahs[i].startPage) {
                return surahs[i]
            }
        }
        return surahs.first()
    }

    fun getParaForPage(page: Int): ParaInfo {
        val clampedPage = page.coerceIn(1, 604)
        return paras.firstOrNull { clampedPage in it.startPage..it.endPage } ?: paras.first()
    }

    val namesOfAllah: List<NameOfAllah> = listOf(
        NameOfAllah(1, "الرَّحْمَنُ", "Ar-Rahman", "The Most Gracious"),
        NameOfAllah(2, "الرَّحِيمُ", "Ar-Raheem", "The Most Merciful"),
        NameOfAllah(3, "الْمَلِكُ", "Al-Malik", "The King / Sovereign"),
        NameOfAllah(4, "الْقُدُّوسُ", "Al-Quddus", "The Most Pure / Holy"),
        NameOfAllah(5, "السَّلَامُ", "As-Salam", "The Source of Peace"),
        NameOfAllah(6, "الْمُؤْمِنُ", "Al-Mu'min", "The Giver of Faith & Security"),
        NameOfAllah(7, "الْمُهَيْمِنُ", "Al-Muhaymin", "The Guardian / Overseer"),
        NameOfAllah(8, "الْعَزِيزُ", "Al-Aziz", "The All-Mighty"),
        NameOfAllah(9, "الْجَبَّارُ", "Al-Jabbar", "The Restorer / Compeller"),
        NameOfAllah(10, "الْمُتَكَبِّرُ", "Al-Mutakabbir", "The Supreme"),
        NameOfAllah(11, "الْخَالِقُ", "Al-Khaliq", "The Creator"),
        NameOfAllah(12, "الْبَارِئُ", "Al-Bari", "The Evolver"),
        NameOfAllah(13, "الْمُصَوِّرُ", "Al-Musawwir", "The Fashioner"),
        NameOfAllah(14, "الْغَفَّارُ", "Al-Ghaffar", "The Perpetual Forgiver"),
        NameOfAllah(15, "الْقَهَّارُ", "Al-Qahhar", "The Subduer"),
        NameOfAllah(16, "الْوَهَّابُ", "Al-Wahhab", "The Bestower"),
        NameOfAllah(17, "الرَّزَّاقُ", "Ar-Razzaq", "The Provider"),
        NameOfAllah(18, "الْفَتَّاحُ", "Al-Fattah", "The Opener / Judge"),
        NameOfAllah(19, "الْعَلِيمُ", "Al-Aleem", "The All-Knowing"),
        NameOfAllah(20, "الْقَابِضُ", "Al-Qabid", "The Withholder")
    )

    val tajweedRules: List<TajweedRule> = listOf(
        TajweedRule("Ghunnah (غنة)", "Nasal Sound", "Holding the nasal sound for 2 counts on Noon and Meem with Shaddah (نّ / مّ).", "إِنَّ الَّذِينَ"),
        TajweedRule("Qalqalah (قلقلة)", "Echo / Bouncing", "Bouncing sound when letters of Qutb Jad (ق, ط, ب, ج, د) have Sukoon.", "قُلْ هُوَ اللَّهُ أَحَدْ"),
        TajweedRule("Idgham (إدغام)", "Merging", "Merging Nun Sakinah or Tanween into letters of Yarmaloon (ي, ر, م, ل, و, ن).", "مَن يَقُولُ"),
        TajweedRule("Ikhfa (إخفاء)", "Concealing", "Concealing Nun Sakinah or Tanween with a light nasal sound before 15 letters.", "مِن قَبْلِكُمْ"),
        TajweedRule("Iqlab (إقلاب)", "Conversion", "Turning Nun Sakinah or Tanween into a Meem when followed by Ba (ب).", "مِن بَعْدِ"),
        TajweedRule("Madd Tabee'ee (مد طبيعي)", "Natural Elongation", "Lengthening the vowel sound for 2 counts on Alif, Waw, or Ya.", "قَالَ / قِيلَ")
    )
}
