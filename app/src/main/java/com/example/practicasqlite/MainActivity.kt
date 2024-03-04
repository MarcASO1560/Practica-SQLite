package com.example.practicasqlite

import android.os.Bundle
import androidx.compose.runtime.livedata.observeAsState
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.practicasqlite.ui.theme.Pink40
import com.example.practicasqlite.ui.theme.PracticaSQLiteTheme

var textName = mutableStateOf("")
var textDescription = mutableStateOf("")
var textYearRelease  = mutableStateOf("")
val genres = listOf(
	"Acción", "Aventura", "RPG", "Simulación", "Estrategia",
	"Deportes", "Carreras", "Puzzle", "Horror"
)
// Estado para controlar el género seleccionado
var selectedGenre = mutableStateOf("Género")

const val ADD_GAME_ROUTE = "add_game"
const val VIDEOGAME_LIBRARY_ROUTE = "videogame_library"

// Estado para controlar la visibilidad del menú desplegable
var expanded = mutableStateOf(false)


class MainActivity : ComponentActivity() {
	@OptIn(ExperimentalMaterial3Api::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			PracticaSQLiteTheme {
				MainScreen(viewModel())
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: VideogameLibraryViewModel) {
	val navController = rememberNavController()
	Scaffold(
		topBar = {
			GenericTopBar(Title = "VIDEOJUEGOS") // El título puede ser dinámico según la selección
		},
		bottomBar = {
			BottomNavigationBar(navController)
		}
	) { innerPadding ->
		NavHost(navController, startDestination = ADD_GAME_ROUTE, Modifier.padding(innerPadding)) {
			composable(ADD_GAME_ROUTE) {
				AddNewCollection()
			}
			composable(VIDEOGAME_LIBRARY_ROUTE) {
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericTopBar(Title: String) {
	CenterAlignedTopAppBar(
		title = {
			Text(
				text = Title,
				fontWeight = FontWeight.Bold
			)
		},
		modifier = Modifier.shadow(10.dp)
	)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewCollection() {
	val backgroundColor = if (isSystemInDarkTheme()) {
		Color.DarkGray // Color para tema oscuro
	} else {
		Color.LightGray // Color para tema claro
	}
	val scrollState = rememberScrollState()
	Box(
		modifier = Modifier
			.padding(20.dp)
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier
				.verticalScroll(scrollState) // Agregar desplazamiento vertical aquí
		) {
			TextField(
				value = textName.value,
				onValueChange = { textName.value = it },
				label = { Text("Nombre") },
				modifier = Modifier
					.padding(10.dp)
					.fillMaxWidth()
			)
			Row {
				Box(
					modifier = Modifier
						.padding(10.dp)
						.weight(1.5f)
						.height(55.dp)
						.clip(shape = RoundedCornerShape(5.dp))
						.background(backgroundColor)
						.fillMaxWidth()
						.clickable { expanded.value = true }
				) {
					Text(
						text = selectedGenre.value,
						fontWeight = FontWeight.Bold,
						modifier = Modifier
							.padding(10.dp)
							.fillMaxSize()
							.wrapContentSize(align = Alignment.CenterStart)
					)
					Icon(Icons.Default.ArrowDropDown, "Desplegar", modifier = Modifier.padding(10.dp).fillMaxSize().wrapContentSize(align = Alignment.CenterEnd))
					DropdownMenu(
						expanded = expanded.value,
						onDismissRequest = { expanded.value = false },
						modifier = Modifier.verticalScroll(rememberScrollState()).width(215.dp).heightIn(0.dp, 200.dp)
					) {
						genres.forEach { genre ->
							DropdownMenuItem(
								text = { Text(genre,fontWeight = FontWeight.Bold) },
								onClick = {
									selectedGenre.value = genre
									expanded.value = false
								},
								modifier = Modifier.fillMaxWidth().wrapContentSize(align = Alignment.Center)
							)
						}
					}
				}
				TextField(
					value = textYearRelease.value,
					onValueChange = { textYearRelease.value = it },
					label = { Text("Año de salida") },
					modifier = Modifier
						.padding(10.dp)
						.weight(1f)
						.fillMaxWidth(),
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Especifica el teclado numérico
				)
			}
			TextField(
				value = textDescription.value,
				onValueChange = { textDescription.value = it },
				label = { Text("Descripción") },
				modifier = Modifier
					.padding(10.dp)
					.fillMaxWidth()
					.height(150.dp)
			)
			ButtonCreateColection() // Asegúrate de que esta función no tiene un Modifier que restrinja su tamaño de manera que no permita el desplazamiento
		}
	}
}
@Composable
fun ButtonCreateColection() {
	Row {
		// Botón para limpiar los campos
		Box(modifier = Modifier
			.fillMaxWidth()
			.weight(1f)
			.padding(10.dp)
			.wrapContentSize(Alignment.CenterStart)) {
			IconButton(
				onClick = {
					textName.value = ""
					selectedGenre.value = "Género"
					textDescription.value = ""
					textYearRelease.value = ""
				},
				modifier = Modifier
					.size(50.dp)
					.shadow(elevation = 5.dp, shape = CircleShape)
					.clip(shape = CircleShape)
					.background(Pink40)
			) {
				Icon(Icons.Filled.Clear, contentDescription = "Clear", tint = Color.White)
			}
		}

		// Contexto local para mostrar Toasts
		val context = LocalContext.current

		// Botón para guardar la información
		Box(modifier = Modifier
			.fillMaxWidth()
			.weight(1f)
			.padding(10.dp)
			.wrapContentSize(Alignment.CenterEnd)) {
			Button(
				onClick = {
					// Verificación de que todos los campos están llenos
					if (textName.value.isNotEmpty() && !selectedGenre.value.equals("Género") && textYearRelease.value.isNotEmpty()) {
						val year = textYearRelease.value.toIntOrNull()
						if (year != null) {
							Toast.makeText(context, "Juego guardado", Toast.LENGTH_SHORT).show()
						} else {
							Toast.makeText(context, "Año de lanzamiento no es válido.", Toast.LENGTH_LONG).show()
						}
					} else {
						Toast.makeText(context, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show()
					}
				},
				modifier = Modifier
					.height(50.dp)
					.width(150.dp)
					.shadow(elevation = 5.dp, shape = CircleShape)
			) {
				Text(text = "Guardar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
			}
		}
	}
}
@Composable
fun BottomNavigationBar(navController: NavController) {
	val items = listOf(
		NavigationItem.Add,
		NavigationItem.VideoGames,
	)
	val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
	NavigationBar(modifier = Modifier.shadow(10.dp)) {
		items.forEach { item ->
			NavigationBarItem(
				icon = { Icon(item.icon, contentDescription = item.title) },
				label = { Text(item.title) },
				selected = currentRoute == item.route,
				onClick = {
					if (item.route == navController.currentDestination?.route) {
						// No hagas nada si el usuario selecciona la sección en la que ya se encuentra
						return@NavigationBarItem
					}
					if (item.route == VIDEOGAME_LIBRARY_ROUTE) {
						// Navega a la biblioteca de videojuegos
						navController.navigate(item.route)
					} else if (item.route == ADD_GAME_ROUTE) {
						// Navega a la sección de añadir videojuegos
						navController.navigate(item.route)
					}
				}
			)
		}
	}
}

enum class NavigationItem(var title: String, var icon: ImageVector, var route: String) {
	Add("Añadir", Icons.Default.AddCircle, ADD_GAME_ROUTE),
	VideoGames("Videojuegos", Icons.Default.List, VIDEOGAME_LIBRARY_ROUTE)
}
@Composable
fun getThemeColor(): Map<String, Color> {
	val colors = if (isSystemInDarkTheme()) {
		mapOf(
			"Rosa" to Color(0xFFCC7B85),
			"Azul" to Color(0xFF809BAA),
			"Verde" to Color(0xFF5DA15D),
			"Lavanda" to Color(0xFFB0A1C5),
			"Amarillo" to Color(0xFFCAC667),
			"Melocoton" to Color(0xFFCCB693),
			"Menta" to Color(0xFF88C9A1),
			"Lila" to Color(0xFFA99CAC),
			"Cielo" to Color(0xFF6DAAC3),
			"Coral" to Color(0xFFC9756E)
		)
	} else {
		mapOf(
			"Rosa" to Color(0xFFFFC0CB),
			"Azul" to Color(0xFFAEC6CF),
			"Verde" to Color(0xFF77DD77),
			"Lavanda" to Color(0xFFE6E6FA),
			"Amarillo" to Color(0xFFFDFD96),
			"Melocoton" to Color(0xFFFFE5B4),
			"Menta" to Color(0xFFAAF0D1),
			"Lila" to Color(0xFFCDB4DB),
			"Cielo" to Color(0xFF87CEEB),
			"Coral" to Color(0xFFF88379)
		)
	}
	return colors
}

@Composable
fun VideoGameList(viewModel: VideogameLibraryViewModel) {
	val videoGames = viewModel.allVideoGames.observeAsState(listOf()).value

	LazyColumn {
		items(videoGames) { videoGame ->
			VideoGameRow(videoGame = videoGame)
		}
	}
}

@Composable
fun VideoGameRow(videoGame: VideoGame) { // Cambia el tipo aquí a VideoGame
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
	) {
		Text(text = videoGame.name, fontWeight = FontWeight.Bold)
		Spacer(Modifier.width(8.dp))
		Text(text = videoGame.genre)
		Spacer(Modifier.width(8.dp))
		Text(text = "${videoGame.yearRelease}")
	}
}