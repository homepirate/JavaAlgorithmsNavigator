import java.util.*;

public class NavigatorImpl implements Navigator {
    private HashTable<Route> routesTable;
    private HashTable<Route> favoriteRoutes;
    private HashTable<Route> topRoutes;

    public NavigatorImpl() {
        routesTable = new HashTable<>();
        favoriteRoutes = new HashTable<>();
        topRoutes = new HashTable<>();
    }

    @Override
    public void addRoute(Route route) {
        routesTable.add(route.getId(), route);
        if (route.isFavorite()) {
            favoriteRoutes.add(route.getId(), route);
        }
    }

    @Override
    public void removeRoute(String routeId) {
        Route route = routesTable.get(routeId);
        if (route != null) {
            routesTable.remove(routeId);
            favoriteRoutes.remove(routeId);
            topRoutes.remove(routeId);
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
            topRoutes.add(route.getId(), route);
        }
    }

    @Override
    public Iterable<Route> searchRoutes(String startPoint, String endPoint) {
        List<Route> matchingRoutes = new ArrayList<>();
        List<Route> favoriteRoutes = new ArrayList<>();

        for (Entry<Route> entry : routesTable) {
            List<String> locationPoints = entry.getValue().getLocationPoints();
            if (locationPoints.get(0).equals(startPoint) && !locationPoints.get(0).equals(endPoint) && locationPoints.contains(endPoint)) {
                Route route = entry.getValue();
                if (route.isFavorite()) {
                    favoriteRoutes.add(route);
                } else {
                    matchingRoutes.add(route);
                }
            }
        }

        Collections.sort(favoriteRoutes,  (r1, r2) -> Integer.compare(r2.getPopularity(), r1.getPopularity()));

        Collections.sort(matchingRoutes, (r1, r2) -> Integer.compare(r2.getPopularity(), r1.getPopularity()));

        List<Route> result = new ArrayList<>();
        result.addAll(favoriteRoutes);
        result.addAll(matchingRoutes);

        return result;
    }





    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        List<Route> matchingRoutes = new ArrayList<>();
        for (Entry<Route> entry : favoriteRoutes) {
            List<String> locationPoints = entry.getValue().getLocationPoints();
            if (!locationPoints.get(0).equals(destinationPoint) && locationPoints.contains(destinationPoint)) {
                matchingRoutes.add(entry.getValue());
            }
        }

        Collections.sort(matchingRoutes, Comparator.comparingDouble(Route::getDistance));

        Collections.sort(matchingRoutes, (r1, r2) -> Integer.compare(r2.getPopularity(), r1.getPopularity()));

        return matchingRoutes;
    }


    @Override
    public Iterable<Route> getTop3Routes() {
        List<Route> topRoutesList = new ArrayList<>(topRoutes.size());

        // Сначала добавляем все маршруты из topRoutes в список
        for (Entry<Route> entry : topRoutes) {
            topRoutesList.add(entry.getValue());
        }

        // Сортируем список по популярности (по убыванию)
        Collections.sort(topRoutesList, (r1, r2) -> Integer.compare(r2.getPopularity(), r1.getPopularity()));

        // Затем сортируем список по расстоянию (по возрастанию)
        Collections.sort(topRoutesList, Comparator.comparingDouble(Route::getDistance));

        // В конце сортируем список по количеству точек местоположения (по возрастанию)
        Collections.sort(topRoutesList, Comparator.comparingInt(route -> route.getLocationPoints().size()));

        if (topRoutesList.size() > 3) {
            topRoutesList = topRoutesList.subList(0, 3);
        }

        return topRoutesList;
    }


}
