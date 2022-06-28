import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class Flows {

  private val v1 = MutableStateFlow(0.0)
  private val v2 = MutableStateFlow(0.0)

  private val valuesFlow = MutableStateFlow(
    listOf(v1.asStateFlow(), v2.asStateFlow()),
  ).asStateFlow()

  @OptIn(ExperimentalCoroutinesApi::class)
  val combination = valuesFlow.flatMapLatest { items ->
    combine(
      items,
      Array<Double>::sum
    )
  }

  val _generation = MutableStateFlow(0)
  val generation = _generation.asStateFlow()

  suspend fun next() {
    v1.value = v1.value + 1
    delay(1)
    v2.value = v2.value + 1
    _generation.value += 1
  }

  suspend fun prev() {
    v1.value = v1.value - 1
    delay(1)
    v2.value = v2.value - 1
    _generation.value -= 1
  }

}


@Composable
fun DisplayFlows(flows: Flows) {
  val coroutineScope = rememberCoroutineScope()
  val value by remember(flows.combination) {
    flows.combination.stateIn(
      scope = coroutineScope,
      started = SharingStarted.WhileSubscribed(),
      initialValue = 0.0
    )
  }.collectAsState()

  println("Value: $value")
  Text(
    text = "Value: ${value.roundToInt()}"
  )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ModifyFlows(flows: Flows) {
  val focusRequester = remember { FocusRequester() }
  val coroutineScope = rememberCoroutineScope()
  Box(
    modifier = Modifier
      .size(120.dp, 100.dp)
      .background(Color.LightGray)
      .focusRequester(focusRequester)
      .focusable()
//      .focusTarget()
      .onKeyEvent {
        if (it.type == KeyEventType.KeyUp) {
          if (it.key == Key.DirectionLeft) {
            coroutineScope.launch {
              flows.prev()
            }
          } else if (it.key == Key.DirectionRight) {
            coroutineScope.launch {
              flows.next()
            }
          }
        }

        true
      }
  ) {
    val generation by flows.generation.collectAsState()
    Text(
      text = "Press left or right\nGeneration: $generation",
      modifier = Modifier.align(Alignment.Center)
    )
  }

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
}

fun main() {
  singleWindowApplication(
    title = "Test",
    state = WindowState(
      width = 400.dp,
      height = 600.dp,
    )
  ) {
    val flows = remember { Flows() }
    Column(
      modifier = Modifier.padding(16.dp).fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      DisplayFlows(flows)
      ModifyFlows(flows)
      Button(
        onClick = {}
      ) {
        Text("Move mouse over me\nto repaint value")
      }
    }
  }
}
