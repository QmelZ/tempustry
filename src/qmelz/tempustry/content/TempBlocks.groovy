package qmelz.tempustry.content

import arc.struct.Seq
import mindustry.ctype.ContentList
import mindustry.world.meta.BuildVisibility

import static mindustry.content.Blocks.*

class TempBlocks implements ContentList{
    void load(){
        Seq.with(duct, ductBridge, ductRouter).each{
            it.buildVisibility = BuildVisibility.shown;
        }
    }
}