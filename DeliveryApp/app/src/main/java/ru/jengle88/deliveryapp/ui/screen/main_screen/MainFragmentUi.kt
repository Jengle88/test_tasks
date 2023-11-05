package ru.jengle88.deliveryapp.ui.screen.main_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import ru.jengle88.deliveryapp.R
import ru.jengle88.deliveryapp.ui.screen.common_component.shimmerEffect
import ru.jengle88.deliveryapp.ui.screen.main_screen.view_object.MealItem
import ru.jengle88.deliveryapp.ui.screen.main_screen.view_object.PromoMealImageItem
import ru.jengle88.deliveryapp.ui.theme.colorAccentBackground
import ru.jengle88.deliveryapp.ui.theme.colorAccentPrimary
import ru.jengle88.deliveryapp.ui.theme.colorCommonBackground
import ru.jengle88.deliveryapp.ui.theme.colorDivider
import ru.jengle88.deliveryapp.ui.theme.colorInactiveFilterBackground
import ru.jengle88.deliveryapp.ui.theme.colorInactiveFilterText
import ru.jengle88.deliveryapp.ui.theme.colorPrimaryText
import ru.jengle88.deliveryapp.ui.theme.colorSecondaryText
import ru.jengle88.deliveryapp.ui.theme.colorShadow

@Composable
fun MainFragmentUi(viewModel: MainFragmentViewModel) {
    val selectedFilterIndex by viewModel.selectedCategoryState.collectAsStateWithLifecycle()
    val mealsCategories by viewModel.mealsCategoriesState.collectAsStateWithLifecycle()
    val meals by viewModel.visibleMealsState.collectAsStateWithLifecycle()
    val mealsLoadingState by viewModel.mealsLoadingState.collectAsStateWithLifecycle()
    val promoMealItems by viewModel.promoMealItemsState.collectAsStateWithLifecycle()

    val citiesList = viewModel.citiesList
    val currentCity by viewModel.currentCityState.collectAsStateWithLifecycle()

    FoodListUi(
        currentCity,
        citiesList,
        selectedFilterIndex,
        mealsCategories,
        mealsLoadingState,
        meals,
        promoMealItems,
        viewModel::onCurrentCityChanged,
        viewModel::onSelectedFilterChanged,
        viewModel::onReloadClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodListUi(
    currentCity: String,
    citiesList: ImmutableList<String>,
    selectedFilterIndex: Int,
    mealsCategories: ImmutableList<String>,
    mealsLoadingState: LoadingState,
    meals: ImmutableList<MealItem>,
    promoMealItems: ImmutableList<PromoMealImageItem>,
    onCurrentCityChanged: (Int) -> Unit,
    onSelectedFilterChanged: (Int) -> Unit,
    onReloadMealsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            FoodTopBar(citiesList, currentCity, onCurrentCityChanged)
        }
    ) { paddingValues ->
        val lazyListState = rememberLazyListState()
        val isShadowForFoodFilterVisible by remember {
            derivedStateOf {
                lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.key == "FilterRowList"
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = lazyListState
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                PromoFoodRowList(promoMealItems)
            }
            stickyHeader("FilterRowList") {
                FilterRowList(
                    selectedFilterIndex,
                    isShadowForFoodFilterVisible,
                    mealsCategories,
                    onSelectedFilterChanged
                )
            }
            when (mealsLoadingState) {
                LoadingState.LOADING -> {
                    items(5) {
                        Divider(thickness = 1.dp, color = colorDivider)
                        FakeMealListItem()
                    }
                }

                LoadingState.SUCCESS -> {
                    items(meals) { mealItem ->
                        Divider(thickness = 1.dp, color = colorDivider)
                        MealListItem(mealItem)
                    }
                }

                LoadingState.FAILURE -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                modifier = Modifier.size(48.dp),
                                onClick = onReloadMealsClick
                            ) {
                                Icon(
                                    modifier = Modifier.size(48.dp),
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }

                LoadingState.NONE -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodTopBar(
    citiesList: ImmutableList<String>,
    currentCity: String,
    onCurrentCityChanged: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Column {
                Row(
                    modifier = Modifier.clickable { expanded = true },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentCity,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            color = colorPrimaryText,
                        )
                    )
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    citiesList.forEachIndexed { index, city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                onCurrentCityChanged(index)
                                expanded = false
                            }
                        )
                    }
                }
            }
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_qr_code),
                    contentDescription = null,
                    contentScale = ContentScale.None
                )
            }
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MealListItem(
    mealItem: MealItem
) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        GlideSubcomposition(
            model = mealItem.imageUrl,
            modifier = Modifier
                .padding(end = 22.dp)
                .size(135.dp),
        ) {
            when (state) {
                RequestState.Loading -> {
                    ProgressIndicator(Modifier.size(135.dp))
                }

                is RequestState.Success -> {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                    )
                }

                else -> {}
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = mealItem.title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700),
                    color = colorPrimaryText,
                ),
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = mealItem.ingredients,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                    color = colorSecondaryText,
                ),
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd,
            ) {
                OutlinedButton(
                    border = BorderStroke(1.dp, colorAccentPrimary),
                    onClick = {},
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "от ${mealItem.price} р",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight(400),
                            color = colorAccentPrimary,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun FakeMealListItem() {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        ProgressIndicator(
            modifier = Modifier
                .padding(end = 22.dp)
                .size(135.dp),
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmerEffect(),
                text = "",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700),
                    color = colorPrimaryText,
                ),
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmerEffect(),
                text = "",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                    color = colorSecondaryText,
                ),
            )
        }
    }
}

@Composable
fun FilterRowList(
    selectedFilterIndex: Int,
    isShadowForFoodFilterVisible: Boolean,
    filtersForFoodTitles: ImmutableList<String>,
    onSelectedFilterChanged: (Int) -> Unit
) {
    if (filtersForFoodTitles.isEmpty()) {
        return
    }

    val modifier = if (isShadowForFoodFilterVisible) {
        Modifier.shadow(
            elevation = 10.dp,
        )
    } else {
        Modifier
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(colorCommonBackground)
            .padding(vertical = 16.dp)
            .height(32.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        filtersForFoodTitles.forEachIndexed { index, title ->
            item {
                if (selectedFilterIndex == index) {
                    SelectedFilterItem(title, index, onSelectedFilterChanged)
                } else {
                    UnselectedFilterItem(title, index, onSelectedFilterChanged)
                }
            }
        }
    }
}


@Composable
fun SelectedFilterItem(
    title: String,
    index: Int,
    onSelectedFilterChanged: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 88.dp, height = 32.dp)
            .background(
                color = colorAccentBackground,
                shape = RoundedCornerShape(size = 6.dp)
            )
            .clickable { onSelectedFilterChanged(index) },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight(600),
                color = colorAccentPrimary
            ),
        )
    }
}

@Composable
fun UnselectedFilterItem(
    title: String,
    index: Int,
    onSelectedFilterChanged: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(size = 6.dp)
            )
            .size(width = 88.dp, height = 32.dp)
            .background(
                color = colorInactiveFilterBackground,
                shape = RoundedCornerShape(size = 6.dp)
            )
            .clickable { onSelectedFilterChanged(index) },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight(400),
                color = colorInactiveFilterText
            ),
        )
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun PromoFoodRowList(promoMealItems: ImmutableList<PromoMealImageItem>) {
    if (promoMealItems.isEmpty()) {
        return
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(promoMealItems) { promoFoodItem ->
            GlideSubcomposition(
                model = promoFoodItem.imageUrl,
                modifier = Modifier
                    .size(width = 300.dp, height = 112.dp)
                    .shadow(
                        elevation = 10.dp,
                        spotColor = colorShadow,
                        ambientColor = colorShadow
                    ),
            ) {
                when (state) {
                    RequestState.Loading -> {
                        ProgressIndicator(
                            modifier = Modifier
                                .size(width = 300.dp, height = 112.dp)
                                .clip(RoundedCornerShape(10.dp)),
                        )
                    }

                    is RequestState.Success -> {
                        Image(
                            modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun ProgressIndicator(modifier: Modifier) {
    Box(
        modifier = modifier.shimmerEffect(),
        contentAlignment = Alignment.Center
    ) {}
}

@Preview
@Composable
fun PreviewFoodListUi() {
    FoodListUi(
        currentCity = "Москва",
        citiesList = persistentListOf("Москва", "Санкт-Петербург", "Казань", "Воронеж", "Орёл"),
        selectedFilterIndex = 0,
        mealsCategories = listOf(
            "Всё",
            "Пицца",
            "Комбо",
            "Десерты",
            "Напитки"
        ).toPersistentList(),
        mealsLoadingState = LoadingState.SUCCESS,
        meals = buildList {
            repeat(20) { i ->
                add(
                    MealItem(
                        id = "$i",
                        title = "Пицца #$i",
                        category = "All",
                        ingredients = "Ingredient #$i",
                        price = 345,
                        imageUrl = "https://drive.google.com/uc?export=download&id=1FUQ6QG96hNu_TAlkXXNA-b3n4lxM5Ohd"
                    )
                )
            }
        }.toPersistentList(),
        promoMealItems = persistentListOf(
            PromoMealImageItem("https://drive.google.com/uc?id=1JTawuyeHCPL8FgH6OzZt4suwUjJpSxoP"),
            PromoMealImageItem("https://drive.google.com/uc?id=1Q9nlg8rQZr2AcpSU_9wYxPD0smOCDSiW"),
            PromoMealImageItem("https://drive.google.com/uc?id=1JTawuyeHCPL8FgH6OzZt4suwUjJpSxoP"),
            PromoMealImageItem("https://drive.google.com/uc?id=1Q9nlg8rQZr2AcpSU_9wYxPD0smOCDSiW"),
        ),
        onSelectedFilterChanged = {},
        onCurrentCityChanged = {},
        onReloadMealsClick = {}
    )
}