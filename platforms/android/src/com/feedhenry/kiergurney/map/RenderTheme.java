package com.feedhenry.kiergurney.map;

import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by kxiang on 24/02/2014.
 */
public class RenderTheme implements XmlRenderTheme {
    public static RenderTheme INSTANCE=new RenderTheme();
    private RenderTheme(){}
    @Override
    public String getRelativePathPrefix() {
        return "/assets/osmarender/";
    }

    // get xml not from within jar but from assets directory
    // whole theme folder was copied into assets
    @Override
    public InputStream getRenderThemeAsStream()
    {
        return getClass().getResourceAsStream("/assets/osmarender/osmarender.xml");
    }
}
