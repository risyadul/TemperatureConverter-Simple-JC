package com.dicoding.mytemperatureconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.mytemperatureconverter.ui.theme.MyTemperatureConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTemperatureConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        StatefulTemperatureInput()
                        TwoWayConverterApp()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun StatefulTemperatureInput(modifier: Modifier = Modifier) {
    var input by remember {
        mutableStateOf("")
    }
    var output by remember {
        mutableStateOf("")
    }
    StatelessTemperatureInput(input = input, output = output, modifier = modifier) {
        input = it
        output = convertToFahrenheit(input)
    }
}

@Composable
fun StatelessTemperatureInput(
    modifier: Modifier = Modifier,
    input: String,
    output: String,
    onValueChanged: (String) -> Unit
) {
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.stateful_converter),
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = input,
            onValueChange = {
                onValueChanged.invoke(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = stringResource(id = R.string.enter_celsius)) }
        )
        Text(text = stringResource(R.string.temperature_fahrenheit, output))
    }
}

@Composable
fun GeneralTemperatureInput(
    scale: Scale,
    input: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = input,
            onValueChange = {
                onValueChange.invoke(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text(
                    text = stringResource(
                        id = R.string.enter_temperature,
                        scale.scaleName
                    )
                )
            }
        )
    }
}

@Composable
fun TwoWayConverterApp(modifier: Modifier = Modifier) {
    var celcius by remember {
        mutableStateOf("")
    }
    var fahrenheit by remember {
        mutableStateOf("")
    }
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.two_way_converter),
            style = MaterialTheme.typography.headlineSmall
        )
        GeneralTemperatureInput(scale = Scale.CELSIUS, input = celcius, onValueChange = {
            celcius = it
            fahrenheit = convertToFahrenheit(celcius)
        })
        GeneralTemperatureInput(scale = Scale.FAHRENHEIT, input = fahrenheit, onValueChange = {
            fahrenheit = it
            celcius = convertToCelsius(fahrenheit)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyTemperatureConverterTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun StatefulTemperatureInputPreview() {
    MyTemperatureConverterTheme {
        StatefulTemperatureInput()
    }
}

private fun convertToFahrenheit(celsius: String): String {
    return (celsius.toDoubleOrNull()?.let { number ->
        (number * 9 / 5) + 32
    } ?: 0).toString()
}

private fun convertToCelsius(fahrenheit: String) =
    (fahrenheit.toDoubleOrNull()?.let {
        (it - 32) * 5 / 9
    } ?: 0).toString()

enum class Scale(val scaleName: String) {
    CELSIUS("Celsius"),
    FAHRENHEIT("Fahrenheit")
}