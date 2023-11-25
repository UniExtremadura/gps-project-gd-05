package com.example.example

import com.google.gson.annotations.SerializedName


data class ColorsResponse(
  @SerializedName("count"    ) var count    : Int?               = null,
  @SerializedName("next"     ) var next     : String?            = null,
  @SerializedName("previous" ) var previous : String?            = null,
  @SerializedName("results"  ) var results  : ArrayList<ColorApi> = arrayListOf()
)
data class ColorApi (
  @SerializedName("id"           ) var id          : Int?         = null,
  @SerializedName("name"         ) var name        : String?      = null,
  @SerializedName("rgb"          ) var rgb         : String?      = null,
  @SerializedName("is_trans"     ) var isTrans     : Boolean?     = null,
  @SerializedName("external_ids" ) var externalIds : ExternalIds? = ExternalIds()
)
data class ExternalIds (
  @SerializedName("BrickOwl" ) var BrickOwl : BrickOwl? = BrickOwl(),
  @SerializedName("LEGO"     ) var LEGO     : LEGO?     = LEGO(),
  @SerializedName("Peeron"   ) var Peeron   : Peeron?   = Peeron(),
  @SerializedName("LDraw"    ) var LDraw    : LDraw?    = LDraw()
)
data class BrickOwl (
  @SerializedName("ext_ids"    ) var extIds    : ArrayList<Int>               = arrayListOf(),
  @SerializedName("ext_descrs" ) var extDescrs : ArrayList<ArrayList<String>> = arrayListOf()
)
data class LDraw (
  @SerializedName("ext_ids"    ) var extIds    : ArrayList<Int>               = arrayListOf(),
  @SerializedName("ext_descrs" ) var extDescrs : ArrayList<ArrayList<String>> = arrayListOf()
)
data class LEGO (
  @SerializedName("ext_ids"    ) var extIds    : ArrayList<Int>               = arrayListOf(),
  @SerializedName("ext_descrs" ) var extDescrs : ArrayList<ArrayList<String>> = arrayListOf()
)
data class Peeron (
  @SerializedName("ext_ids"    ) var extIds    : ArrayList<String>            = arrayListOf(),
  @SerializedName("ext_descrs" ) var extDescrs : ArrayList<ArrayList<String>> = arrayListOf()
)
