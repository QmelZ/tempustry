package qmelz.tempustry.content

import arc.struct.Seq
import arc.util.OS
import mindustry.content.TechTree
import mindustry.content.TechTree.TechNode
import mindustry.ctype.ContentList
import mindustry.ctype.UnlockableContent
import mindustry.game.Objectives
import mindustry.type.ItemStack

import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue

import static mindustry.type.ItemStack.with
import static mindustry.content.Items.*
import static mindustry.content.Blocks.*

// multithreaded techtree loading, possibly faster than vanilla mindus
class TempTechTree implements ContentList{
    private static exec = Executors.newFixedThreadPool((OS.cores / 2) as int);
    private static nodes = new LinkedBlockingQueue<Future>();
    
    void load(){
        nodeFrom(invertedSorter, duct, duct.researchRequirements(), null, {
            it.node(ductRouter, {
                it.node(ductBridge);
            });
        });
        
        waitFor();
    }
    
    static void waitFor(){
        // probably bad idea but who cares
        while(nodes.poll().get());
    }
    
    static void nodeFrom(UnlockableContent parent, UnlockableContent content, ItemStack[] requirements, Seq<Objectives.Objective> objectives, Closure children){
        def context = TechTree.get(parent);
        TempTechNode node = new TempTechNode(context, content, requirements);
        if(objectives != null){
            node.objectives.addAll(objectives);
        }
        
        get(node, children);
    }
    
    static void get(TempTechNode node, Closure closure){
        nodes.add exec.submit{
            closure.call(node);
        }
    }
    
    static class TempTechNode extends TechNode{
        TempTechNode(TechNode parent, UnlockableContent content, ItemStack[] requirements){
            super(parent, content, requirements)
        }
        
        void node(
            UnlockableContent content,
            ItemStack[] requirements = content.researchRequirements(),
            Seq<Objectives.Objective> objectives = null,
            Closure children = {}
        ){
            TempTechNode node = new TempTechNode(this, content, requirements);
            if(objectives != null){
                node.objectives.addAll(objectives);
            }
    
            get(node, children);
        }
        
        void node(UnlockableContent content, ItemStack[] requirements, Closure children){
            node(content, requirements, null, children);
        }
        
        void node(UnlockableContent content, Closure children){
            node(content, content.researchRequirements(), children);
        }
    }
}
