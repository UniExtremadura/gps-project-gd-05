package com.gd05.brickr.data.api

import com.google.gson.annotations.SerializedName

data class BrickSetBricksResponse (

    @SerializedName("count"    ) var count    : Int?               = null,
    @SerializedName("next"     ) var next     : String?            = null,
    @SerializedName("previous" ) var previous : String?            = null,
    @SerializedName("results"  ) var results  : ArrayList<Results> = arrayListOf()

)

data class PartExternalIds (

    @SerializedName("BrickLink" ) var BrickLink : ArrayList<String> = arrayListOf(),
    @SerializedName("BrickOwl"  ) var BrickOwl  : ArrayList<String> = arrayListOf(),
    @SerializedName("Brickset"  ) var Brickset  : ArrayList<String> = arrayListOf(),
    @SerializedName("LDraw"     ) var LDraw     : ArrayList<String> = arrayListOf(),
    @SerializedName("LEGO"      ) var LEGO      : ArrayList<String> = arrayListOf()

)

data class Part (

    @SerializedName("part_num"     ) var partNum     : String,
    @SerializedName("name"         ) var name        : String?      = null,
    @SerializedName("part_cat_id"  ) var partCatId   : Int?         = null,
    @SerializedName("part_url"     ) var partUrl     : String?      = null,
    @SerializedName("part_img_url" ) var partImgUrl  : String?      = null,
    @SerializedName("external_ids" ) var externalIds : PartExternalIds? = PartExternalIds(),
    @SerializedName("print_of"     ) var printOf     : String?      = null

)

data class BrickLink (

    @SerializedName("ext_ids"    ) var extIds    : ArrayList<Int>               = arrayListOf(),
    @SerializedName("ext_descrs" ) var extDescrs : ArrayList<ArrayList<String>> = arrayListOf()

)

data class BrickOwl (

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

data class LDraw (

    @SerializedName("ext_ids"    ) var extIds    : ArrayList<Int>               = arrayListOf(),
    @SerializedName("ext_descrs" ) var extDescrs : ArrayList<ArrayList<String>> = arrayListOf()

)

data class ColorExternalIds (

    @SerializedName("BrickLink" ) var BrickLink : BrickLink? = BrickLink(),
    @SerializedName("BrickOwl"  ) var BrickOwl  : BrickOwl?  = BrickOwl(),
    @SerializedName("LEGO"      ) var LEGO      : LEGO?      = LEGO(),
    @SerializedName("Peeron"    ) var Peeron    : Peeron?    = Peeron(),
    @SerializedName("LDraw"     ) var LDraw     : LDraw?     = LDraw()

)

data class Color (

    @SerializedName("id"           ) var id          : Int?         = null,
    @SerializedName("name"         ) var name        : String?      = null,
    @SerializedName("rgb"          ) var rgb         : String?      = null,
    @SerializedName("is_trans"     ) var isTrans     : Boolean?     = null,
    @SerializedName("external_ids" ) var externalIds : ColorExternalIds? = ColorExternalIds()

)

data class Results (

    @SerializedName("id"          ) var id        : Int?     = null,
    @SerializedName("inv_part_id" ) var invPartId : Int?     = null,
    @SerializedName("part"        ) var part      : Part,
    @SerializedName("color"       ) var color     : Color?   = Color(),
    @SerializedName("set_num"     ) var setNum    : String?  = null,
    @SerializedName("quantity"    ) var quantity  : Int,
    @SerializedName("is_spare"    ) var isSpare   : Boolean? = null,
    @SerializedName("element_id"  ) var elementId : String?  = null,
    @SerializedName("num_sets"    ) var numSets   : Int?     = null

)