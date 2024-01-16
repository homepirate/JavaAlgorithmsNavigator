import java.util.*;
import java.util.stream.Collectors;

public class NavigatorImpl implements Navigator {
    private HashTable<Route> routesTable;


    public NavigatorImpl() {
        routesTable = new HashTable<>();
    }

    @Override
    public void addRoute(Route route) {
        boolean isDuplicate = false;

        for (Route existingRoute : routesTable.values()) {
            if (existingRoute.equals(route)) {
                isDuplicate = true;
                break;
            }
        }
        if (!isDuplicate) {
            routesTable.add(route.getId(), route);
        }
    }

    @Override
    public void removeRoute(String routeId) {
        Route route = routesTable.get(routeId);
        if (route != null) {
            routesTable.remove(routeId);
        }
    }

    @Override
    public boolean contains(Route route) {
        return routesTable.find(route.getId()) != null;
    }

    @Override
    public int size() {
        return routesTable.size();
    }

    @Override
    public Route getRoute(String routeId) {
        return routesTable.get(routeId);
    }

    @Override
    public void chooseRoute(String routeId) {
        Route route = routesTable.get(routeId);
        if (route != null) {
            route.setPopularity(route.getPopularity() + 1);
        }
    }

    @Override
    public Iterable<Route> searchRoutes(String startPoint, String endPoint) {
        List<Route> matchingRoutes = new ArrayList<>();
        List<Route> favoriteRoutes = new ArrayList<>();

        for (Entry<Route> entry : routesTable) {
            List<String> locationPoints = entry.getValue().getLocationPoints();
//            if (locationPoints.get(0).equals(startPoint) && !locationPoints.get(0).equals(endPoint) && locationPoints.contains(endPoint)) {
            if (locationPoints.get(0).equals(startPoint) && !locationPoints.get(0).equals(endPoint) && locationPoints.get(locationPoints.size()-1).equals(endPoint)) {
                Route route = entry.getValue();
                if (route.isFavorite()) {
                    favoriteRoutes.add(route);
                } else {
                    matchingRoutes.add(route);
                }
            }
        }


//        favoriteRoutes.sort(Comparator.comparingInt((Route r) -> r.getLocationPoints().size())
//                .thenComparingInt(Route::getPopularity).reversed());
//
//        matchingRoutes.sort(Comparator.comparingInt((Route r) -> r.getLocationPoints().size())
//                .thenComparingInt(Route::getPopularity).reversed());


        favoriteRoutes.sort(Comparator.comparingDouble((Route r) -> r.getDistance())
                .thenComparingInt(Route::getPopularity).reversed());

        matchingRoutes.sort(Comparator.comparingDouble((Route r) -> r.getDistance())
                .thenComparingInt(Route::getPopularity).reversed());


        List<Route> result = new ArrayList<>();
        result.addAll(favoriteRoutes);
        result.addAll(matchingRoutes);


        //        отбрасываем дубликаты
        result = result.stream().distinct().collect(Collectors.toList());

        return result;
    }





    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        List<Route> matchingRoutes = new ArrayList<>();
        for (Entry<Route> entry : routesTable) {
            List<String> locationPoints = entry.getValue().getLocationPoints();
            if (entry.getValue().isFavorite() && !locationPoints.get(0).equals(destinationPoint) && locationPoints.contains(destinationPoint)) {
                matchingRoutes.add(entry.getValue());
            }
        }

        Collections.sort(matchingRoutes, Comparator.comparingDouble(Route::getDistance).reversed()
                .thenComparingInt(Route::getPopularity).reversed());

        //        отбрасываем дубликаты
        matchingRoutes = matchingRoutes.stream().distinct().collect(Collectors.toList());

        return matchingRoutes;
    }



    @Override
    public Iterable<Route> getTop3Routes() {
        List<Route> topRoutesList = new ArrayList<>();

        for (Entry<Route> entry : routesTable) {
            topRoutesList.add(entry.getValue());
        }




        Collections.sort(topRoutesList, (r1, r2) -> {
            // Сначала сортируем список по популярности
            int popularityComparison = Integer.compare(r2.getPopularity(), r1.getPopularity());
            if (popularityComparison != 0) {
                return popularityComparison;
            }

            // Затем сортируем список по расстоянию (по возрастанию)
            int distanceComparison = Double.compare(r1.getDistance(), r2.getDistance());
            if (distanceComparison != 0) {
                return distanceComparison;
            }

            // В конце сортируем список по количеству точек местоположения (по возрастанию)
            return Integer.compare(r1.getLocationPoints().size(), r2.getLocationPoints().size());
        });

        if (topRoutesList.size() > 3) {
            topRoutesList = topRoutesList.subList(0, 3);
        }


        //        отбрасываем дубликаты
        topRoutesList = topRoutesList.stream().distinct().collect(Collectors.toList());

        return topRoutesList;
    }


}
