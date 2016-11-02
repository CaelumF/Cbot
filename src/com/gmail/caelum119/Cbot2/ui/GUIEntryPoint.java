package com.gmail.caelum119.Cbot2.ui;

import com.coderdojo.prototype_rw_controller.KClassesToClassesKt;
import com.gmail.caelum119.Cbot2.IRCCBot;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;
import tornadofx.App;
import tornadofx.View;

/**
 * First created 4/8/2016 in CoderdojoRobotController
 */
public class GUIEntryPoint extends App{
    public GUIEntryPoint(){

    }

    @NotNull @Override public KClass<? extends View> getPrimaryView(){
        return KClassesToClassesKt.getRWController();
    }

    public static void main(String[] args){
        new Thread(new IRCCBot());
        launch(args);
    }
}
