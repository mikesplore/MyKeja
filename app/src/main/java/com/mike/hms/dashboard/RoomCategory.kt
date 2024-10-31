package com.mike.hms.dashboard

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mike.hms.model.houseModel.HouseCategory
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.houseModel.HouseViewModel
import java.util.Locale
import kotlin.text.category
import kotlin.toString
import com.mike.hms.ui.theme.CommonComponents as CC


val houseCategory = listOf(
    HouseCategory.ALL,
    HouseCategory.DELUXE,
    HouseCategory.ECONOMY,
    HouseCategory.FAMILY_SUITE,
    HouseCategory.LUXURY,
    HouseCategory.STANDARD
)

object FilteredCategory {
    val category: MutableState<String> = mutableStateOf("")
}

@Composable
fun HouseCategoryBox(houseCategory: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) CC.extraPrimaryColor() else CC.surfaceContainerColor(),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .clip(RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = houseCategory,
            style = CC.contentTextStyle().copy(
                color = if (isSelected) CC.primaryColor() else CC.secondaryColor()
            ),
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
fun HousesCategory() {
    var selectedIndex by remember { mutableIntStateOf(-1) } // Track the selected index

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(houseCategory.size) { index ->
            val formattedCategory = houseCategory[index]
                .toString()
                .replace("_", " ") // Replace underscore with space
                .lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

            HouseCategoryBox(
                houseCategory = formattedCategory,
                isSelected = selectedIndex == index, // Check if this box is selected
                onClick = {
                    FilteredCategory.category.value = formattedCategory
                    selectedIndex = index
                } // Update selected index when clicked
            )
        }
    }
}


@Composable
fun HouseCategoryItem(house: HouseEntity, modifier: Modifier = Modifier, navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val boxWidth = screenWidth * 0.35f
    val boxHeight = boxWidth * 1.3f
    val density = LocalDensity.current
    val textSize = with(density) { (boxHeight * 0.1f).toSp() }

    // Card for the house item
    Card(
        modifier = modifier
            .width(boxWidth)
            .height(boxHeight),
        shape = RoundedCornerShape(20.dp),  // Rounded corners
        elevation = CardDefaults.cardElevation(5.dp)  // Built-in shadow/elevation
    ) {
        Box {
            // House image
            AsyncImage(
                model = house.houseImageLink.randomOrNull(),
                contentDescription = "House Image",
                modifier = Modifier
                    .clickable{
                        navController.navigate("houseDetails/${house.houseID}")
                    }
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            // Display the house price at the bottom end
            Box(
                modifier = Modifier
                    .background(CC.secondaryColor(), shape = RoundedCornerShape(10.dp))
                    .align(Alignment.TopEnd)  // Align to the bottom end
            ) {
                Text(
                    text = String.format(Locale.getDefault(), "Ksh %,d/Night", house.housePrice),
                    style = CC.bodyTextStyle().copy(
                        fontSize = textSize * 0.7f,
                        color = CC.primaryColor()
                    ),
                    modifier = Modifier
                        .padding(5.dp)
                )
            }

            // Bottom overlay for house details (type, capacity, rating)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)  // Align the other details to the bottom start
                    .fillMaxWidth()
                    .background(
                        CC
                            .surfaceContainerColor()
                            .copy(alpha = 0.9f)
                    )
                    .padding(5.dp)  // Padding for a cleaner layout
            ) {
                // House type and capacity
                Text(
                    text = house.houseType.name.first()
                        .uppercase(Locale.getDefault()) + house.houseType.name.substring(1)
                        .lowercase(Locale.getDefault()),
                    style = CC.bodyTextStyle().copy(
                        fontSize = textSize * 0.7f,
                        color = CC.tertiaryColor()
                    ),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
                val guests =
                    if (house.houseCapacity == 1) " ${house.houseCapacity} Guest" else " ${house.houseCapacity} Guests"
                Text(
                    text = guests,
                    style = CC.bodyTextStyle().copy(
                        fontSize = textSize * 0.7f,
                        color = CC.extraPrimaryColor()
                    ),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }
    }
}


@Composable
fun RecommendedHouseTypeList(modifier: Modifier = Modifier, navController: NavController) {
    val houseViewModel: HouseViewModel = hiltViewModel()
    val houses by houseViewModel.houses.collectAsState()
    val configuration = LocalConfiguration.current

    val filteredHouses = if (FilteredCategory.category.value.isEmpty() || FilteredCategory.category.value.toString() == "All") {
        houses // Don't filter if category is empty
    } else {
        houses.filter { house ->
            house.houseCategory
                .toString()
                .replace("_", " ")
                .lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } == FilteredCategory.category.value
        }
    }

    LazyRow(
        modifier = modifier
            .animateContentSize()
            .padding(start = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if (filteredHouses.isEmpty() && !(FilteredCategory.category.value.isEmpty() || FilteredCategory.category.value.toString() == "All")) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(configuration.screenHeightDp.dp * 0.15f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Sorry, no houses found in this category", style = CC.titleTextStyle())
                }
            }
        } else {
            items(filteredHouses) { houseCategory ->
                AnimatedVisibility(visible = filteredHouses.isNotEmpty(),
                    exit = fadeOut(animationSpec = tween(500)),
                    enter = fadeIn(animationSpec = tween(500))
                ) {
                HouseCategoryItem(houseCategory, navController = navController)
                }
            }
        }
    }
}


