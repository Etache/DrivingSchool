package com.example.drivingschool.ui.fragments.enroll

data class MockCommentModel (
    val profilePhoto: String,
    val name: String,
    val surname: String,
    val commentText: String
)

val commentsList = listOf<MockCommentModel>(
    MockCommentModel("https://icdn.lenta.ru/images/2022/07/14/13/20220714133343910/detail_ce1ea0e09434874de43adf7225d10bfe.jpg", "Giorno", "Giovanna", "завтра экзамен, за 2 часа объяснил все что можно. Дал уверенности, что я сдам"),
    MockCommentModel("https://upload.wikimedia.org/wikipedia/commons/7/74/Elon_Musk_Colorado_2022_%28cropped_2%29.jpg", "Илон", "Маск", "отличный инструктор, может понятно объяснить мои ошибки, даже в напряженный момент во время вождения"),
    MockCommentModel("https://i.ytimg.com/vi/ofHe_3tW3-s/hqdefault.jpg", "Фанта", "Пепси", "хороший инструктор, но 4 звезды, потому что он нет чернокожих инструкторов, вы что расисты? еще одна проблема, я делал запись на 3 часа, но ездили мы час, а остальные 2 часа он сидел в чайхане с друзьями")
)