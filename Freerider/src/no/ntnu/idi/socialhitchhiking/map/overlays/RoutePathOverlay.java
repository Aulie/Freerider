/**
 * @contributor(s): Freerider Team (Group 4, IT2901 Fall 2012, NTNU)
 * @version: 		1.0
 *
 * Copyright (C) 2012 Freerider Team.
 *
 * Licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package no.ntnu.idi.socialhitchhiking.map.overlays;

import java.util.List;

import no.ntnu.idi.freerider.model.Location;
import no.ntnu.idi.socialhitchhiking.map.GeoHelper;
import no.ntnu.idi.socialhitchhiking.map.MapRoute;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * An {@link Overlay} that displays the route on the map.
 */
public class RoutePathOverlay extends Overlay {
	 
    private int pathColor;
    private final List<Location> routePath;
    private final int STROKE_WIDTH = 7;

    public RoutePathOverlay(List<Location> routePath) {
            this(routePath, Color.BLUE);
    }
    public RoutePathOverlay(MapRoute route) {
        this(route.getRouteData());
    }

    public RoutePathOverlay(List<Location> routePath, int pathColor) {
            this.routePath = routePath;
            this.pathColor = pathColor;
    }
   
    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
            if (shadow == false && routePath != null) {
                    Path path = new Path();
                    //We are creating the path
                    for (int i = 0; i < routePath.size(); i++) {
                            GeoPoint gPointA = GeoHelper.getGeoPoint(routePath.get(i));
                            Point pointA = new Point();
                            mapView.getProjection().toPixels(gPointA, pointA);
                            
                            if (i == 0) { //This is the start point
                            	path.moveTo(pointA.x, pointA.y);
                            } else {
                            	path.lineTo(pointA.x, pointA.y);
                            }
                    }

                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(pathColor);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(STROKE_WIDTH);
                    paint.setAlpha(120);

                    if (!path.isEmpty()) canvas.drawPath(path, paint);
            }
            return super.draw(canvas, mapView, shadow, when);
    }
}