package com.example.banksafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// IMPORTACIÓN CORREGIDA: Acceso a los recursos R.drawable.xxx
import com.example.banksafe.R

// Define tus recursos (ya se resuelven con la importación de R)
object Drawables {
    val shirt_blue = R.drawable.shirt_blue
    val shirt_slim = R.drawable.shirt_slim
    val shirt_stack = R.drawable.shirt_stack
    val dress = R.drawable.dress
}

// Modelos de Datos
data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val imageResId: Int,
    val detailImageIds: List<Int> = listOf(imageResId, imageResId)
)

data class ProductDetail(
    val name: String,
    val price: String,
    val sizes: List<String>,
    val detailImageIds: List<Int>
)

data class CarouselItem(val id: Int, val title: String, val imageResId: Int)

// Datos de Muestra
val sampleProducts = listOf(
    Product(1, "Cotton Shirt", "$24.00", Drawables.shirt_blue, listOf(Drawables.shirt_blue, Drawables.shirt_slim)),
    Product(2, "Slim Fit Shirt", "$15.00", Drawables.shirt_slim, listOf(Drawables.shirt_slim, Drawables.shirt_blue)),
    Product(3, "Stack Shirts", "$30.00", Drawables.shirt_stack),
    Product(4, "Red Dress", "$55.00", Drawables.dress)
)

val detailData = ProductDetail(
    name = "Cotton Shirt",
    price = "$24.00",
    sizes = listOf("S", "M", "L", "XL"),
    detailImageIds = listOf(Drawables.shirt_blue, Drawables.shirt_slim)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Banksafe()
            }
        }
    }
}

// ----------------------------------------------------
// NAVEGACIÓN
// ----------------------------------------------------

@Composable
fun Banksafe() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "productList") {

        // Vista 1: Lista de Productos
        composable("productList") {
            ProductListScreen(navController = navController, carouselRoute = "carouselScreen")
        }

        // Vista 2: Detalle del Producto
        composable(
            route = "productDetail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 1
            val product = sampleProducts.find { it.id == productId }

            if (product != null) {
                val detail = ProductDetail(
                    name = product.name,
                    price = product.price,
                    sizes = listOf("S", "M", "L", "XL"),
                    detailImageIds = product.detailImageIds
                )
                ProductDetailScreen(navController = navController, detail = detail)
            } else {
                Text("Error: Producto no encontrado")
            }
        }

        // NUEVA RUTA: Pantalla del Carrusel
        composable("carouselScreen") {
            SimpleCarouselScreen(navController = navController, productListRoute = "productList")
        }
    } // <-- LLAVE DE CIERRE FALTANTE EN EL NavHost (CORREGIDA)
}

// ----------------------------------------------------
// VISTA 1: LISTA DE PRODUCTOS (ProductListScreen)
// ----------------------------------------------------

@Composable
fun ProductListScreen(navController: NavController, carouselRoute: String) {
    Scaffold(
        topBar = { ProductListTopBar(navController) },
        bottomBar = { AppBottomBar() },
        modifier = Modifier.background(Color(0xFF202020))
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .background(Color(0xFF202020))) {

            // Botón para navegar al carrusel
            Button(
                onClick = { navController.navigate(carouselRoute) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Ir a Carrusel Simple")
            }

            // Fila de Filtros (simulada)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DEPARTMENT ⌵",
                    color = Color.White
                )
                Text(
                    text = "FILTER & SORT ☰",
                    color = Color.White
                )
            }

            // Grid de Productos
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleProducts) { product ->
                    ProductItem(product = product) {
                        navController.navigate("productDetail/${product.id}")
                    }
                }
            }

            Button(
                onClick = { navController.popBackStack("productList", inclusive = false) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Volver a Lista (Botón de Pila)")
            }
        }
    }
}

@Composable
fun ProductListTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF202020))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.clickable { navController.popBackStack() }
        )
        Text(
            text = "SHIRTS",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.White
        )
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        // Área de la imagen con el ícono de corazón
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFEDE0D4)) // Color de fondo claro
        ) {
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Texto
        Text(text = product.name, color = Color.White)
        Text(text = product.price, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

// ----------------------------------------------------
// VISTA 2: DETALLES DEL PRODUCTO (ProductDetailScreen)
// ----------------------------------------------------

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(navController: NavController, detail: ProductDetail) {
    val pagerState = rememberPagerState(pageCount = { detail.detailImageIds.size })

    Scaffold(
        topBar = { DetailTopBar(navController) },
        bottomBar = { DetailBottomBar(detail.price) }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color(0xFFEDE0D4))) { // Fondo beige

            // Carrusel de Imágenes con Indicadores
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                HorizontalPager(state = pagerState) { page ->
                    Image(
                        painter = painterResource(id = detail.detailImageIds[page]),
                        contentDescription = detail.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Indicadores de Página (los pequeños círculos)
                Row(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color = if (pagerState.currentPage == iteration) Color.White else Color.Gray
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }
            }

            // Información del Producto
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "SKIRTS", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text(text = detail.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                // Tallas y Precio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Size: ", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                        detail.sizes.forEach { size ->
                            Text(
                                text = size,
                                modifier = Modifier.padding(horizontal = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = detail.price,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun DetailTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent) // Se verá el color de la imagen
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.clickable { navController.popBackStack() }
        )
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = "Favorite",
            tint = Color.White
        )
    }
}

@Composable
fun DetailBottomBar(price: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Button(
            onClick = { /* Lógica de añadir a la bolsa */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)) // Color azul
        ) {
            Text("Add to Bag", style = MaterialTheme.typography.titleLarge)
        }
    }
}


@Composable
fun AppBottomBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF202020)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color.White
        )
    }
}

/**
 * Crea un carrusel con desplazamiento horizontal eficiente usando LazyRow.
 * @param items La lista de datos CarouselItem a mostrar.
 */
@Composable
fun HorizontalCarousel(items: List<CarouselItem>) {
    LazyRow(
        // Añade relleno (padding) a los lados del carrusel
        contentPadding = PaddingValues(horizontal = 16.dp),
        // Define el espacio entre cada tarjeta
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Itera sobre la lista de ítems para construir las tarjetas
        items(items) { item ->
            CarouselCard(item = item)
        }
    }
}

/**
 * Define la apariencia de cada tarjeta dentro del carrusel.
 */
@Composable
fun CarouselCard(item: CarouselItem) {
    Card(
        modifier = Modifier
            // Define el tamaño fijo de cada tarjeta para que quepan varias en la fila
            .width(200.dp)
            .height(180.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                // Usa el ID de recurso para cargar la imagen
                painter = painterResource(id = item.imageResId),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.title,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

// NUEVA PANTALLA: Contenedor para el carrusel simple (CORREGIDA)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCarouselScreen(navController: NavController, productListRoute: String) {
    // Datos de ejemplo para el carrusel simple
    val carouselItems = remember {
        listOf(
            CarouselItem(1, "Oferta 1: Camisa Azul", Drawables.shirt_blue),
            CarouselItem(2, "Oferta 2: Camisa Ajustada", Drawables.shirt_slim),
            CarouselItem(3, "Oferta 3: Pila de Camisas", Drawables.shirt_stack),
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Carrusel de Ofertas") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Botón para navegar a la lista de productos
            Button(
                onClick = { navController.navigate(productListRoute) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Volver a Lista de Productos")
            }

            Text(
                text = "Ofertas Destacadas (LazyRow)",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HorizontalCarousel(items = carouselItems)

            Spacer(modifier = Modifier.height(32.dp))

            Text("Desplázate para ver más", color = Color.Gray)
        }
    }
}