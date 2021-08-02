package qmelz.tempustry

import arc.struct.Seq
import mindustry.ctype.ContentList
import mindustry.mod.Mod
import qmelz.tempustry.content.TempBlocks
import qmelz.tempustry.content.TempTechTree

class Tempustry extends Mod{
    Seq<ContentList> content = [
        new TempBlocks(),
        new TempTechTree()
    ];
    
    void loadContent(){
        content.each{it.load()}
    }
    
    void init(){}
}
