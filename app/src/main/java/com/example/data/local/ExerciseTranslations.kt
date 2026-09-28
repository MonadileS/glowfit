package com.example.data.local

import com.example.data.model.ExerciseLanguage

/**
 * Multi-language step-by-step instruction dataset supporting 10 languages:
 * 🇬🇧 English, 🇪🇸 Spanish, 🇫🇷 French, 🇮🇹 Italian, 🇹🇷 Turkish,
 * 🇷🇺 Russian, 🇨🇳 Chinese, 🇮🇳 Hindi, 🇵🇱 Polish, 🇰🇷 Korean.
 * Sourced & aligned with the exercises-dataset specifications.
 */
object ExerciseTranslations {

  val supportedLanguages: List<ExerciseLanguage> = listOf(
    ExerciseLanguage.ENGLISH,
    ExerciseLanguage.SPANISH,
    ExerciseLanguage.FRENCH,
    ExerciseLanguage.ITALIAN,
    ExerciseLanguage.TURKISH,
    ExerciseLanguage.RUSSIAN,
    ExerciseLanguage.CHINESE,
    ExerciseLanguage.HINDI,
    ExerciseLanguage.POLISH,
    ExerciseLanguage.KOREAN
  )

  private val translations: Map<String, Map<String, List<String>>> = mapOf(
    "ex_russian_twist" to mapOf(
      "en" to listOf(
        "Sit on the mat with knees bent and feet slightly off the floor.",
        "Lean your torso back at a 45-degree angle, keeping your spine neutral.",
        "Rotate your torso smoothly from side to side, engaging your obliques.",
        "Gently touch the floor beside each hip and breathe continuously."
      ),
      "es" to listOf(
        "Siéntate en la colchoneta con las rodillas flexionadas y los pies ligeramente elevados.",
        "Inclina el torso hacia atrás en un ángulo de 45 grados manteniendo la espalda recta.",
        "Gira el torso suavemente de lado a lado contrayendo los oblicuos.",
        "Toca suavemente el suelo al lado de cada cadera mientras respiras de forma fluida."
      ),
      "fr" to listOf(
        "Asseyez-vous sur le tapis, genoux fléchis et pieds légèrement décollés du sol.",
        "Inclinez votre buste vers l'arrière à 45 degrés en gardant le dos bien droit.",
        "Pivotez votre torse d'un côté à l'autre en contractant intensément vos obliques.",
        "Touchez délicatement le sol à côté de chaque hanche en respirant régulièrement."
      ),
      "it" to listOf(
        "Siediti sul tappetino con le ginocchia piegate e i piedi leggermente sollevati.",
        "Inclina il busto all'indietro a 45 gradi mantenendo la schiena dritta.",
        "Ruota il busto fluidamente da un lato all'altro contraendo gli obliqui.",
        "Tocca leggermente il pavimento accanto a ciascun fianco espirando."
      ),
      "tr" to listOf(
        "Matın üzerine oturun, dizlerinizi bükün ve ayaklarınızı hafifçe yerden kaldırın.",
        "Omurganızı dik tutarak gövdenizi 45 derece geriye doğru eğin.",
        "Yan karın kaslarınızı sıkarak gövdenizi bir sağa bir sola yavaşça döndürün.",
        "Her dönüşte ellerinizi kalçanızın yanındaki zemine hafifçe dokundurun."
      ),
      "ru" to listOf(
        "Сядьте на коврик, согните колени и слегка приподнимите стопы от пола.",
        "Отклоните корпус назад под углом 45 градусов с прямой спиной.",
        "Плавно поворачивайте корпус из стороны в сторону, напрягая косые мышцы пресса.",
        "Касайтесь пола сбоку от каждого бедра, сохраняя ровное дыхание."
      ),
      "zh" to listOf(
        "坐在瑜伽垫上，屈膝，双脚可稍微离地或轻放于地面。",
        "躯干向后倾斜约45度，保持脊柱挺直，收紧核心肌群。",
        "平稳地左右旋转躯干，充分收紧侧腹肌。",
        "每次旋转时双手轻触臀部两侧地面，保持均匀呼吸。"
      ),
      "hi" to listOf(
        "चटाई पर बैठें, घुटने मोड़ें और पैरों को फर्श से थोड़ा ऊपर उठाएं।",
        "रीढ़ को सीधा रखते हुए अपने धड़ को 45 डिग्री के कोण पर पीछे झुकाएं।",
        "कमर की मांसपेशियों को कसते हुए धड़ को बाईं और दाईं ओर घुमाएं।",
        "हर बार कूल्हे के पास फर्श को धीरे से छुएं और सांस लेते रहें।"
      ),
      "pl" to listOf(
        "Usiądź na macie z ugiętymi kolanami i stopami lekko uniesionymi nad podłogę.",
        "Odchyl tułów do tyłu pod kątem 45 stopni, zachowując prosty kręgosłup.",
        "Obracaj tułów płynnie na boki, napinając mięśnie skośne brzucha.",
        "Dotykaj dłońmi maty obok każdego biodra, oddychając miarowo."
      ),
      "ko" to listOf(
        "매트에 앉아 무릎을 구부리고 발을 바닥에서 살짝 띄웁니다.",
        "등을 곧게 편 상태에서 상체를 45도 각도로 뒤로 젖힙니다.",
        "복사근에 긴장을 유지하며 상체를 좌우로 부드럽게 회전합니다.",
        "각 회전 시 양손으로 골반 옆 바닥을 가볍게 터치합니다."
      )
    ),

    "ex_bicycle_crunches" to mapOf(
      "en" to listOf(
        "Lie on your back with knees bent at 90 degrees, hands behind your head.",
        "Lift your shoulders off the floor and engage your abdominal wall.",
        "Pedal your legs while bringing opposite elbow toward opposite knee.",
        "Alternate sides in a smooth, controlled rhythm without pulling on the neck."
      ),
      "es" to listOf(
        "Túmbate boca arriba con las rodillas a 90 grados y las manos tras la cabeza.",
        "Eleva los hombros del suelo activando el abdomen.",
        "Pedalea con las piernas llevando el codo opuesto hacia la rodilla contraria.",
        "Alterna los lados con un ritmo suave y controlado sin tirar del cuello."
      ),
      "fr" to listOf(
        "Allongez-vous sur le dos, genoux pliés à 90 degrés, mains derrière la tête.",
        "Décollez les omoplates du sol en engageant profondément le centre du corps.",
        "Pédalez avec les jambes en amenant le coude opposé vers le genou opposé.",
        "Alternez de façon fluide et contrôlée sans tirer sur la nuque."
      ),
      "it" to listOf(
        "Sdraiati sulla schiena con le ginocchia a 90 gradi e le mani dietro la nuca.",
        "Solleva le spalle dal pavimento attivando i muscoli addominali.",
        "Pedala con le gambe portando il gomito opposto verso il ginocchio opposto.",
        "Alterna i lati in modo fluido e controllato senza forzare il collo."
      ),
      "tr" to listOf(
        "Sırt üstü uzanın, dizlerinizi 90 derece bükün, ellerinizi başınızın arkasına koyun.",
        "Kürek kemiklerinizi yerden kaldırarak karın kaslarınızı sıkın.",
        "Bacaklarınızı pedal çevirir gibi hareket ettirirken zıt dirseği zıt dize yaklaştırın.",
        "Boynunuzu çekmeden kontrollü ve akıcı bir şekilde devam edin."
      ),
      "ru" to listOf(
        "Лягте на спину, согните ноги в коленях под углом 90 градусов, руки за головой.",
        "Оторвите лопатки от пола, активируя мышцы пресса.",
        "Выполняйте движение педалирования, направляя локоть к противоположному колену.",
        "Чередуйте стороны плавно и подконтрольно, не напрягая шею."
      ),
      "zh" to listOf(
        "仰卧在垫子上，双膝弯曲90度，双手轻放于耳后。",
        "肩胛骨离开地面，收紧深层腹部肌群。",
        "双腿做踩单车动作，同时用对侧手肘去触碰对侧膝盖。",
        "左右交替进行，保持平稳节奏，不要用力拉拽颈部。"
      ),
      "hi" to listOf(
        "पीठ के बल लेटें, घुटनों को 90 डिग्री पर मोड़ें और हाथों को सिर के पीछे रखें।",
        "कंधों को फर्श से ऊपर उठाएं और पेट की मांसपेशियों को कसें।",
        "विपरीत कोहनी को विपरीत घुटने की ओर लाते हुए पैरों से साइकिल चलाएं।",
        "गर्दन पर दबाव डाले बिना दोनों तरफ बारी-बारी से करें।"
      ),
      "pl" to listOf(
        "Połóż się na plecach z kolanami ugiętymi pod kątem 90 stopni, dłonie za głową.",
        "Unieś łopatki nad matę, napinając mięśnie brzucha.",
        "Wykonuj ruch pedałowania, zbliżając przeciwny łokieć do przeciwnego kolana.",
        "Zmieniaj strony płynnie i pod kontrolą, nie ciągnąc za kark."
      ),
      "ko" to listOf(
        "바닥에 누워 무릎을 90도로 구부리고 손은 머리 뒤에 댑니다.",
        "어깨를 바닥에서 띄우며 복부를 강하게 수축합니다.",
        "자전거 페달을 밟듯 다리를 움직이며 반대쪽 팔꿈치와 무릎을 교차시킵니다.",
        "목을 당기지 않고 부드럽고 일정한 리듬으로 반복합니다."
      )
    ),

    "ex_glute_bridge" to mapOf(
      "en" to listOf(
        "Lie flat on your back with knees bent, feet hip-width on the floor.",
        "Drive through your heels to lift your hips toward the ceiling.",
        "Squeeze your glutes firmly at the top without hyperextending your lower back.",
        "Lower your hips down with control and repeat."
      ),
      "es" to listOf(
        "Túmbate boca arriba con las rodillas dobladas y los pies apoyados al ancho de caderas.",
        "Empuja con los talones para elevar las caderas hacia el techo.",
        "Aprieta fuerte los glúteos arriba sin arquear excesivamente la zona lumbar.",
        "Baja las caderas de forma controlada y repite."
      ),
      "fr" to listOf(
        "Allongez-vous sur le dos, genoux pliés, pieds à plat écartés de la largeur des hanches.",
        "Poussez sur vos talons pour soulever votre bassin vers le haut.",
        "Contractez fermement les fessiers au sommet sans cambrer le bas du dos.",
        "Redescendez doucement le bassin avec contrôle et recommencez."
      ),
      "it" to listOf(
        "Sdraiati sulla schiena con le ginocchia piegate e i piedi alla larghezza delle anche.",
        "Spingi sui talloni per sollevare i fianchi verso il soffitto.",
        "Contrai forte i glutei nella parte superiore senza inarcare la schiena.",
        "Abbassa i fianchi con controllo e ripeti."
      ),
      "tr" to listOf(
        "Sırt üstü uzanın, dizlerinizi bükün ve ayaklarınızı kalça genişliğinde yere basın.",
        "Topuklarınızdan güç alarak kalçanızı yukarı doğru kaldırın.",
        "Zirve noktasında kalça kaslarınızı sıkın, belinizi aşırı bükmeyin.",
        "Kalçanızı kontrollü bir şekilde indirip tekrarlayın."
      ),
      "ru" to listOf(
        "Лягте на спину, согните ноги в коленях, стопы на ширине плеч на полу.",
        "Упираясь пятками в пол, поднимите таз вверх.",
        "Максимально сожмите ягодицы в верхней точке, не переразгибая поясницу.",
        "Плавно опустите таз и повторите движение."
      ),
      "zh" to listOf(
        "仰卧在垫上，屈膝，双脚分开与髋同宽平踏于地面。",
        "脚后跟发力蹬地，将臀部向上推起至身体呈一条直线。",
        "在最高点用力夹紧臀大肌，避免腰部过度反弓。",
        "控制速度缓慢下落臀部，重复动作。"
      ),
      "hi" to listOf(
        "पीठ के बल लेटें, घुटने मोड़ें और पैरों को कूल्हे की चौड़ाई पर फर्श पर रखें।",
        "कूल्हों को ऊपर उठाने के लिए अपनी एड़ियों से जोर लगाएं।",
        "शीर्ष पर नितंबों (glutes) को मजबूती से दबाएं, पीठ को अधिक न मोड़ें।",
        "नियंत्रण के साथ कूल्हों को नीचे लाएं और दोहराएं।"
      ),
      "pl" to listOf(
        "Połóż się na plecach z ugiętymi kolanami, stopy na szerokość bioder na macie.",
        "Wypchnij biodra w górę, dociskając pięty do podłoża.",
        "W górnej pozycji mocno zepnij pośladki, nie wyginając nadmiernie lędźwi.",
        "Opuść biodra powoli pod kontrolą i powtórz."
      ),
      "ko" to listOf(
        "매트에 누워 무릎을 세우고 발을 골반 너비로 바닥에 놓습니다.",
        "발뒤꿈치를 밀며 엉덩이를 천장 쪽으로 높이 들어 올립니다.",
        "최고점에서 둔근을 강하게 조여주고 허리가 꺾이지 않도록 주의합니다.",
        "천천히 골반을 내리며 통제된 동작으로 반복합니다."
      )
    ),

    "ex_donkey_kicks" to mapOf(
      "en" to listOf(
        "Start on all fours with hands under shoulders and knees under hips.",
        "Keeping your knee bent at 90 degrees, kick your right heel up toward the sky.",
        "Squeeze the glute at the peak of the movement while keeping hips square.",
        "Lower back down without touching the floor and complete all reps before switching."
      ),
      "es" to listOf(
        "Comienza en cuadrupedia con manos bajo los hombros y rodillas bajo caderas.",
        "Manteniendo la rodilla a 90 grados, patea con el talón hacia el techo.",
        "Aprieta el glúteo arriba manteniendo las caderas paralelas al suelo.",
        "Baja sin tocar el suelo y completa las repeticiones antes de cambiar."
      ),
      "fr" to listOf(
        "Placez-vous à quatre pattes, mains sous les épaules et genoux sous les hanches.",
        "Genou plié à 90°, poussez votre talon vers le plafond.",
        "Contractez le fessier en haut en gardant les hanches bien droites.",
        "Redescendez sans toucher le sol et répétez avant de changer de côté."
      ),
      "it" to listOf(
        "Inizia a quattro zampe con le mani sotto le spalle e le ginocchia sotto le anche.",
        "Mantenendo il ginocchio piegato a 90 gradi, spingi il tallone verso l'alto.",
        "Contrai il gluteo in alto mantenendo il bacino stabile.",
        "Ritorna senza toccare terra e completa le ripetizioni."
      ),
      "tr" to listOf(
        "Eller omuzların, dizler kalçaların altında olacak şekilde dört ayak pozisyonuna geçin.",
        "Dizinizi 90 derece bükük tutarak sağ topuğunuzu tavana doğru itin.",
        "Zirvede kalçanızı iyice sıkın ve omurganızı düz tutun.",
        "Yere değdirmeden indirin ve diğer tarafa geçmeden tekrarları tamamlayın."
      ),
      "ru" to listOf(
        "Встаньте на четвереньки: ладони под плечами, колени под бёдрами.",
        "Сохраняя угол в колене 90 градусов, вытолкните пятку вверх к потолку.",
        "Максимально сожмите ягодичную мышцу в верхней точке, удерживая таз ровно.",
        "Опустите ногу, не касаясь коврика, и выполните все повторы."
      ),
      "zh" to listOf(
        "四足跪姿，双手在肩部正下方，双膝在髋部正下方。",
        "保持膝盖弯曲90度，将右脚后跟向天花板方向踢起。",
        "在动作最高点夹紧臀部，骨盆保持水平不要倾斜。",
        "缓慢下落膝盖不着地，做完一侧再换另一侧。"
      ),
      "hi" to listOf(
        "हाथों और घुटनों के बल आएं, हाथ कंधों के नीचे और घुटने कूल्हों के नीचे रखें।",
        "घुटने को 90 डिग्री पर रखते हुए, अपनी एड़ी को ऊपर की ओर उठाएं।",
        "शीर्ष पर नितंब को कसें और कूल्हों को सीधा रखें।",
        "बिना फर्श छुए नीचे लाएं और दोनों पैरों से दोहराएं।"
      ),
      "pl" to listOf(
        "Przejdź do klęku podpartego, dłonie pod barkami, kolana pod biodrami.",
        "Utrzymując kąt 90 stopni w kolanie, wypchnij piętę do góry.",
        "Napnij pośladek na szczycie ruchu, trzymając biodra równolegle do maty.",
        "Opuść nogę nie dotykając podłoża i wykonaj serię."
      ),
      "ko" to listOf(
        "손은 어깨 아래, 무릎은 골반 아래에 두고 네발기기 자세를 취합니다.",
        "무릎을 90도로 유지한 채 발뒤꿈치를 천장 쪽으로 힘차게 밀어 올립니다.",
        "골반이 틀어지지 않도록 주의하며 둔근을 최대로 수축합니다.",
        "바닥에 무릎이 닿지 않게 내린 후 반복합니다."
      )
    ),

    "ex_jumping_jacks" to mapOf(
      "en" to listOf(
        "Stand tall with feet together and arms relaxed by your sides.",
        "Jump feet outward while raising arms overhead until hands meet.",
        "Land softly on the balls of your feet with knees slightly unlocked.",
        "Quickly jump back to starting position and maintain a brisk tempo."
      ),
      "es" to listOf(
        "Ponte de pie con los pies juntos y los brazos a los lados.",
        "Salta abriendo las piernas y elevando los brazos sobre la cabeza.",
        "Aterriza suavemente sobre las puntas de los pies con rodillas relajadas.",
        "Vuelve rápidamente a la posición inicial manteniendo un ritmo alegre."
      ),
      "fr" to listOf(
        "Tenez-vous debout, pieds joints et bras le long du corps.",
        "Sautez en écartant les jambes et en levant les bras au-dessus de la tête.",
        "Atterrissez en douceur sur la pointe des pieds, genoux souples.",
        "Revenez en position initiale d'un mouvement dynamique et fluide."
      ),
      "it" to listOf(
        "In piedi con i piedi uniti e le braccia lungo i fianchi.",
        "Salta aprendo le gambe e sollevando le braccia sopra la testa.",
        "Atterra morbidamente sugli avampiedi con le ginocchia flesse.",
        "Ritorna rapidamente alla posizione di partenza con ritmo costante."
      ),
      "tr" to listOf(
        "Ayaklar bitişik, kollar yanlarda dik bir şekilde durun.",
        "Zıplayarak bacakları açın ve kolları başınızın üzerine kaldırın.",
        "Dizlerinizi hafif bükerek ayak parmak uçlarınızda yumuşakça yere basın.",
        "Hızlıca başlangıç pozisyonuna dönün ve ritmi koruyun."
      ),
      "ru" to listOf(
        "Встаньте прямо, ноги вместе, руки вдоль тела.",
        "В прыжке расставьте ноги в стороны и поднимите руки над головой.",
        "Мягко приземляйтесь на носочки с чуть согнутыми коленями.",
        "Быстро вернитесь в исходное положение, сохраняя бодрый темп."
      ),
      "zh" to listOf(
        "自然站立，双脚并拢，双臂自然垂于身体两侧。",
        "双脚向两侧跳开，同时双臂在头顶上方击掌或合拢。",
        "脚前掌轻盈着地，膝盖微屈缓冲压力。",
        "快速跳回起始位置，保持富有弹性的欢快节奏。"
      ),
      "hi" to listOf(
        "पैरों को जोड़कर और हाथों को बगल में रखकर सीधे खड़े हों।",
        "कूदकर पैरों को चौड़ा करें और हाथों को सिर के ऊपर उठाएं।",
        "घुटनों को हल्का मोड़कर पंजों पर धीरे से उतरें।",
        "जल्दी से प्रारंभिक स्थिति में लौटें और लय बनाए रखें।"
      ),
      "pl" to listOf(
        "Stań prosto ze stopami złączonymi i rękami wzdłuż tułowia.",
        "Wyskocz, rozszerzając nogi i unosząc ręce nad głowę.",
        "Ląduj miękko na palcach ze swobodnymi kolanami.",
        "Wróć szybko do pozycji wyjściowej, utrzymując dynamiczne tempo."
      ),
      "ko" to listOf(
        "양발을 모으고 팔은 몸 옆에 두고 곧게 섭니다.",
        "가볍게 점프하며 다리를 벌리고 양팔을 머리 위로 올립니다.",
        "무릎에 무리가 가지 않도록 발 앞꿈치로 가볍게 착지합니다.",
        "빠르게 시작 자세로 돌아오며 리드미컬하게 반복합니다."
      )
    ),

    "ex_cobra_stretch" to mapOf(
      "en" to listOf(
        "Lie face down on the mat with palms flat under your shoulders.",
        "Hug your elbows close to your torso.",
        "Gently press through your palms to lift your chest off the mat.",
        "Keep your shoulders down and breathe deeply into your heart center."
      ),
      "es" to listOf(
        "Túmbate boca abajo con las palmas apoyadas bajo los hombros.",
        "Mantén los codos pegados al cuerpo.",
        "Presiona suavemente las manos para elevar el pecho del suelo.",
        "Relaja los hombros lejos de las orejas y respira profundamente."
      ),
      "fr" to listOf(
        "Allongez-vous sur le ventre, paumes à plat sous les épaules.",
        "Gardez les coudes serrés contre le corps.",
        "Poussez doucement sur les mains pour redresser le buste.",
        "Abaissez les épaules et respirez profondément pour ouvrir la cage thoracique."
      ),
      "it" to listOf(
        "Sdraiati a pancia in giù con le mani sotto le spalle.",
        "Tieni i gomiti vicini al busto.",
        "Spingi delicatamente sulle mani per sollevare il petto dal tappetino.",
        "Rilassa le spalle e fai respiri profondi e rigeneranti."
      ),
      "tr" to listOf(
        "Mat üzerine yüzüstü uzanın, avuç içlerinizi omuzlarınızın altına yerleştirin.",
        "Dirseklerinizi gövdenize yakın tutun.",
        "Avuçlarınızdan hafifçe güç alarak göğsünüzü yukarı doğru kaldırın.",
        "Omuzlarınızı rahat bırakın ve derin nefes alarak göğsünüzü açın."
      ),
      "ru" to listOf(
        "Лягте на живот, ладони расположите под плечами.",
        "Прижмите локти к корпусу.",
        "Мягко оттолкнитесь руками и поднимите грудь от коврика.",
        "Опустите плечи вниз и сделайте глубокий расслабляющий вдох."
      ),
      "zh" to listOf(
        "俯卧在瑜伽垫上，双手手掌平放于肩膀下方。",
        "手肘夹紧身体两侧肋骨。",
        "双手平稳推地，将胸部向上抬离垫面，骨盆保持贴地。",
        "沉肩放松，深呼吸感受腹部与胸腔的舒展伸拉。"
      ),
      "hi" to listOf(
        "पेट के बल लेटें, हथेलियों को कंधों के नीचे रखें।",
        "कोहनियों को शरीर के पास रखें।",
        "हाथों से दबाते हुए छाती को ऊपर उठाएं।",
        "कंधों को आराम दें और गहरी सांस लें।"
      ),
      "pl" to listOf(
        "Połóż się na brzuchu z dłońmi płasko pod barkami.",
        "Trzymaj łokcie blisko tułowia.",
        "Naciśnij dłońmi na matę, unosząc klatkę piersiową w górę.",
        "Opuść barki i weź głęboki, uspokajający oddech."
      ),
      "ko" to listOf(
        "매트에 엎드려 손바닥을 어깨 아래 바닥에 둡니다.",
        "팔꿈치를 몸통 옆에 가깝게 붙입니다.",
        "손바닥으로 바닥을 밀며 상체를 부드럽게 들어 올립니다.",
        "어깨를 낮추고 가슴을 열며 깊고 편안하게 호흡합니다."
      )
    )
  )

  fun getInstructions(exerciseId: String, langCode: String, fallback: List<String>): List<String> {
    // 1. Check in loaded exercise model
    val exercise = ExerciseCatalog.getById(exerciseId)
    val fromExercise = exercise?.multilingualInstructions?.get(langCode)
    if (fromExercise != null && fromExercise.isNotEmpty()) {
      return fromExercise
    }

    // 2. Check in legacy translations map
    val fromMap = translations[exerciseId]?.get(langCode)
    if (fromMap != null && fromMap.isNotEmpty()) {
      return fromMap
    }

    // 3. Fallback to English from model or map or parameter
    val englishFallback = exercise?.multilingualInstructions?.get("en")
      ?: translations[exerciseId]?.get("en")
      ?: exercise?.instructions
      ?: fallback

    return englishFallback
  }

  fun getSupportedLanguagesForExercise(exerciseId: String): List<ExerciseLanguage> {
    val exercise = ExerciseCatalog.getById(exerciseId)
    val exerciseKeys = exercise?.multilingualInstructions?.keys
    if (exerciseKeys != null && exerciseKeys.isNotEmpty()) {
      return supportedLanguages.filter { exerciseKeys.contains(it.code) || it.code == "en" }
    }

    val availableCodes = translations[exerciseId]?.keys ?: setOf("en")
    return supportedLanguages.filter { availableCodes.contains(it.code) || it.code == "en" }
  }
}
