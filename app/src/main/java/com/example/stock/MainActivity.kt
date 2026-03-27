package com.example.stock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stock.data.local.AppDatabase
import com.example.stock.data.local.StockDao
import com.example.stock.ui.detail.DetailScreen
import com.example.stock.ui.detail.DetailViewModel
import com.example.stock.ui.news.NewsScreen
import com.example.stock.ui.news.NewsViewModel
import com.example.stock.ui.search.SearchScreen
import com.example.stock.ui.search.SearchViewModel
import com.example.stock.ui.theme.StockTheme
import com.example.stock.ui.stocklist.StockListScreen
import com.example.stock.ui.stocklist.StockViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StockApp()
                }
            }
        }
    }
}

class DetailViewModelFactory(private val stockDao: StockDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(stockDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class StockViewModelFactory(private val stockDao: StockDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StockViewModel(stockDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun StockApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    val searchViewModel: SearchViewModel = viewModel()
    var selectedSymbol by rememberSaveable { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val stockViewModel: StockViewModel = viewModel(
        factory = StockViewModelFactory(database.stockDao())
    )
    val detailViewModel: DetailViewModel = viewModel(
        factory = DetailViewModelFactory(database.stockDao())
    )
    val newsViewModel: NewsViewModel = viewModel()

    if (selectedSymbol != null) {
        DetailScreen(
            symbol = selectedSymbol!!,
            viewModel = detailViewModel,
            onBack = { selectedSymbol = null }
        )
    }else{
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = it.label
                            )
                        },
                        label = { Text(it.label) },
                        selected = it == currentDestination,
                        onClick = { currentDestination = it }
                    )
                }
            }
        ) {
            when (currentDestination) {
                AppDestinations.HOME -> {
                    StockListScreen(
                        viewModel = stockViewModel,
                        onSelect = { symbol -> selectedSymbol = symbol } // 修改状态！
                    )
                }
                AppDestinations.SEARCH -> {
                    SearchScreen(
                        viewModel = searchViewModel,
                        onSelect = { symbol -> selectedSymbol = symbol } // 修改状态！
                    )
                }
                AppDestinations.NEWS -> NewsScreen(viewModel = newsViewModel)
            }
        }
    }
}

@Composable
fun PlaceholderScreen(text: String) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Text(text = text, modifier = Modifier.padding(16.dp))
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("行情", Icons.Default.Home),
    SEARCH("搜索", Icons.Default.Search),
    NEWS("新闻", Icons.Default.Newspaper),
}