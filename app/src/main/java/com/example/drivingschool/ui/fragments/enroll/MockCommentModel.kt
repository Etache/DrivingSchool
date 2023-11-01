package com.example.drivingschool.ui.fragments.enroll

data class MockCommentModel (
    val profilePhoto: String,
    val name: String,
    val surname: String,
    val commentText: String
)

val commentsList = listOf<MockCommentModel>(
    MockCommentModel("https://icdn.lenta.ru/images/2022/07/14/13/20220714133343910/detail_ce1ea0e09434874de43adf7225d10bfe.jpg", "Giorno", "Giovanna", "он мой отец"),
    MockCommentModel("https://24.kz/media/k2/items/cache/0ea7761dbc0f461ae2d999718aae8816_XL.jpg", "Илон", "Маск", "не нравится, увольняйте"),
    MockCommentModel("https://cdn5.vedomosti.ru/crop/image/2023/6k/17yq5b/original-1kz8.jpg?height=698&width=1240", "Фанта", "Пепси", "хороший инструктор, но 4 звезды, потому что он нет чернокожих инструкторов, вы что расисты? еще одна проблема, я делал запись на 3 часа, но ездили мы час, а остальные 2 часа он сидел в чайхане с друзьями")
)