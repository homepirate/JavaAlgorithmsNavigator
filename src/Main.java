import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Navigator navigator = new NavigatorImpl();

        Route route1 = new Route( "route1", Arrays.asList("A", "B", "C"), true);
        Route route2 = new Route( "route2", Arrays.asList("B", "C", "D"), false);
        Route route3 = new Route("route3", Arrays.asList("A", "C", "E"), true);
        Route route7 = new Route("route7", Arrays.asList("A", "D", "E"), true);
        Route route8 = new Route("route8", Arrays.asList("И", "Ф", "E"), true);
        Route route9 = new Route("route9", Arrays.asList("A", "E", "K", "B"), true);
        Route route10 = new Route("route10", Arrays.asList("E", "K"), true);
        Route route11 = new Route("route11", Arrays.asList("A", "K", "E", "M"), false);
        Route route12 = new Route("route12", Arrays.asList("A", "E", "M"), false);

        Route route4 = new Route("route4", Arrays.asList("C", "D", "K", "F"), true);

        navigator.addRoute(route1);
        navigator.addRoute(route2);
        navigator.addRoute(route3);
        navigator.addRoute(route4);
        navigator.addRoute(route7);
        navigator.addRoute(route8);
        navigator.addRoute(route9);
        navigator.addRoute(route10);
        navigator.addRoute(route11);
        navigator.addRoute(route12);

        System.out.println("Наличие маршрута route1: " + navigator.contains(route1)); // true
        System.out.println("Наличие маршрута route4: " + navigator.contains(route4)); // true

        System.out.println("Размер навигатора: " + navigator.size());

        Route retrievedRoute = navigator.getRoute("route2");
        System.out.println("Маршрут с идентификатором route2: " + retrievedRoute);

        // Повышаем popularity
        navigator.chooseRoute("route3");
        navigator.chooseRoute("route3");
        navigator.chooseRoute("route3");
        navigator.chooseRoute("route3");
        navigator.chooseRoute("route1");
        navigator.chooseRoute("route1");
        navigator.chooseRoute("route1");
        navigator.chooseRoute("route4");
        navigator.chooseRoute("route4");
        navigator.chooseRoute("route4");
        navigator.chooseRoute("route12");


        System.out.println("Обновленный маршрут route3: " + navigator.getRoute("route3")); // Route(id=route3, locationPoints=[A, C, E], favorite=true, popularity=1)

        Iterable<Route> matchingRoutes1 = navigator.searchRoutes("A", "E");
        System.out.println("Маршруты с точками отправления A и прибытия E:");
        for (Route route : matchingRoutes1) {
            System.out.println(route);
        }

        Iterable<Route> favoriteRoutes = navigator.getFavoriteRoutes("E");
        System.out.println("Избранные маршруты для точки прибытия E:");
        for (Route route : favoriteRoutes) {
            System.out.println(route);
        }

        Iterable<Route> top3Routes = navigator.getTop3Routes();
        System.out.println("Топ-3 маршрута:");
        for (Route route : top3Routes) {
            System.out.println(route);
        }

        navigator.removeRoute("route4");
        System.out.println("Размер навигатора после удаления маршрута: " + navigator.size());
    }
}
