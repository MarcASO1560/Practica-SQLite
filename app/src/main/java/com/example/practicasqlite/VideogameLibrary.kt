package com.example.practicasqlite
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practicasqlite.ui.theme.PracticaSQLiteTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VideogameLibrary : ComponentActivity() {
	@OptIn(ExperimentalMaterial3Api::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val viewModel: VideogameLibraryViewModel = ViewModelProvider(this).get(VideogameLibraryViewModel::class.java)
		setContent {
			PracticaSQLiteTheme {
				MainScreen(viewModel)
			}
		}
	}
}
@Entity(tableName = "videogames")
data class VideoGame(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	val name: String,
	val genre: String,
	val yearRelease: Int,
	val description: String
)
@Dao
interface VideoGameDao {
	@Insert
	suspend fun insert(videoGame: VideoGame)

	@Query("SELECT * FROM videogames")
	fun getAll(): LiveData<List<VideoGame>>
}
@Database(entities = [VideoGame::class], version = 1, exportSchema = false)
abstract class VideoGameDatabase : RoomDatabase() {
	abstract fun videoGameDao(): VideoGameDao

	companion object {
		@Volatile
		private var INSTANCE: VideoGameDatabase? = null

		fun getDatabase(context: Context): VideoGameDatabase {
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					VideoGameDatabase::class.java,
					"videogame_database"
				).build()
				INSTANCE = instance
				instance
			}
		}
	}
}
class VideogameLibraryViewModel(application: Application) : AndroidViewModel(application) {
	private val database = VideoGameDatabase.getDatabase(application)
	private val dao = database.videoGameDao()

	val allVideoGames: LiveData<List<VideoGame>> = dao.getAll()

	fun insert(videoGame: VideoGame) = viewModelScope.launch(Dispatchers.IO) {
		dao.insert(videoGame)
	}
}