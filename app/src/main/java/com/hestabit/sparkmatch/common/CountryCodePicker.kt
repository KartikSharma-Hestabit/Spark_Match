package com.hestabit.sparkmatch.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

fun getCountries(): List<Pair<String, String>> {
    val isoCountryCodes: Array<String> = Locale.getISOCountries()
    val countriesWithCodes: MutableList<Pair<String, String>> = mutableListOf()

    for (countryCode in isoCountryCodes) {
        val locale = Locale("", countryCode)
        val countryName: String = locale.displayCountry
        val dialCode = getCountryDialingCode(countryCode)
        if (true) {
            countriesWithCodes.add(countryName to dialCode)
        }
    }
    return countriesWithCodes
}

fun getCountryDialingCode(countryCode: String): String {
    val countryDialingCodes = mapOf(
        "AF" to "+93", "AL" to "+355", "DZ" to "+213", "AD" to "+376", "AO" to "+244",
        "AR" to "+54", "AM" to "+374", "AU" to "+61", "AT" to "+43", "AZ" to "+994",
        "BH" to "+973", "BD" to "+880", "BY" to "+375", "BE" to "+32", "BZ" to "+501",
        "BJ" to "+229", "BT" to "+975", "BO" to "+591", "BA" to "+387", "BW" to "+267",
        "BR" to "+55", "BN" to "+673", "BG" to "+359", "BF" to "+226", "BI" to "+257",
        "KH" to "+855", "CM" to "+237", "CA" to "+1", "CV" to "+238", "CF" to "+236",
        "TD" to "+235", "CL" to "+56", "CN" to "+86", "CO" to "+57", "KM" to "+269",
        "CG" to "+242", "CD" to "+243", "CR" to "+506", "CI" to "+225", "HR" to "+385",
        "CU" to "+53", "CY" to "+357", "CZ" to "+420", "DK" to "+45", "DJ" to "+253",
        "DO" to "+1", "EC" to "+593", "EG" to "+20", "SV" to "+503", "GQ" to "+240",
        "ER" to "+291", "EE" to "+372", "ET" to "+251", "FJ" to "+679", "FI" to "+358",
        "FR" to "+33", "GA" to "+241", "GM" to "+220", "GE" to "+995", "DE" to "+49",
        "GH" to "+233", "GR" to "+30", "GT" to "+502", "GN" to "+224", "GW" to "+245",
        "GY" to "+592", "HT" to "+509", "HN" to "+504", "HU" to "+36", "IS" to "+354",
        "IN" to "+91", "ID" to "+62", "IR" to "+98", "IQ" to "+964", "IE" to "+353",
        "IL" to "+972", "IT" to "+39", "JM" to "+1", "JP" to "+81", "JO" to "+962",
        "KZ" to "+7", "KE" to "+254", "KI" to "+686", "KW" to "+965", "KG" to "+996",
        "LA" to "+856", "LV" to "+371", "LB" to "+961", "LS" to "+266", "LR" to "+231",
        "LY" to "+218", "LI" to "+423", "LT" to "+370", "LU" to "+352", "MG" to "+261",
        "MW" to "+265", "MY" to "+60", "MV" to "+960", "ML" to "+223", "MT" to "+356",
        "MH" to "+692", "MR" to "+222", "MU" to "+230", "MX" to "+52", "FM" to "+691",
        "MD" to "+373", "MC" to "+377", "MN" to "+976", "ME" to "+382", "MA" to "+212",
        "MZ" to "+258", "MM" to "+95", "NA" to "+264", "NR" to "+674", "NP" to "+977",
        "NL" to "+31", "NZ" to "+64", "NI" to "+505", "NE" to "+227", "NG" to "+234",
        "KP" to "+850", "NO" to "+47", "OM" to "+968", "PK" to "+92", "PW" to "+680",
        "PA" to "+507", "PG" to "+675", "PY" to "+595", "PE" to "+51", "PH" to "+63",
        "PL" to "+48", "PT" to "+351", "QA" to "+974", "RO" to "+40", "RU" to "+7",
        "RW" to "+250", "WS" to "+685", "SM" to "+378", "ST" to "+239", "SA" to "+966",
        "SN" to "+221", "RS" to "+381", "SC" to "+248", "SL" to "+232", "SG" to "+65",
        "SK" to "+421", "SI" to "+386", "SB" to "+677", "SO" to "+252", "ZA" to "+27",
        "KR" to "+82", "ES" to "+34", "LK" to "+94", "SD" to "+249", "SR" to "+597",
        "SE" to "+46", "CH" to "+41", "SY" to "+963", "TJ" to "+992", "TZ" to "+255",
        "TH" to "+66", "TL" to "+670", "TG" to "+228", "TO" to "+676", "TN" to "+216",
        "TR" to "+90", "TM" to "+993", "UG" to "+256", "UA" to "+380", "AE" to "+971",
        "GB" to "+44", "US" to "+1", "UY" to "+598", "UZ" to "+998", "VU" to "+678",
        "VE" to "+58", "VN" to "+84", "YE" to "+967", "ZM" to "+260", "ZW" to "+263"
    )
    return countryDialingCodes[countryCode].toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPickerBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            shape = CutoutShape(),
            containerColor = Color.White,
            dragHandle = null,
            properties = ModalBottomSheetProperties(),
            scrimColor = Color.Black.copy(alpha = 0.6f)
        ) {
            var searchText by remember { mutableStateOf("") }
            val countriesWithCodes = getCountries()

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search country") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .height(300.dp)
                        .padding(top = 8.dp)
                ) {
                    val filteredList = if (searchText.isEmpty()) {
                        countriesWithCodes
                    } else {
                        countriesWithCodes.filter {
                            it.first.contains(searchText, ignoreCase = true)
                        }
                    }

                    items(filteredList) { (country, code) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelect(code)
                                    onDismiss()
                                }
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "$country ($code)",
                                fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
