package br.com.dev.tps.precounitario.data

import br.com.dev.tps.precounitario.model.Calculation
import br.com.dev.tps.precounitario.repository.Resources

data class HomeUiState(
    var calculationList: Resources<List<Calculation>> = Resources.Loading(),
    val calculationDeletedStatus: Boolean = false,
)