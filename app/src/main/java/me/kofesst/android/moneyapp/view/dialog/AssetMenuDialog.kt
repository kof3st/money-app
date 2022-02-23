package me.kofesst.android.moneyapp.view.dialog

import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.model.AssetEntity

class AssetMenuDialog(
    private val asset: AssetEntity
): BottomSheetDialog(R.string.asset_menu_title, R.layout.asset_menu_dialog) {

}