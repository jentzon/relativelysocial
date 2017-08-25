'use strict';

// App module

var relativelySocialApp = angular.module('relativelySocialApp', [
    'ngRoute',
    'relativelySocialControllers',
    'authService'
]);

relativelySocialApp.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $routeProvider.
        when('/', {
            templateUrl: 'partials/main.html',
            controller: 'MainCtrl'
        })
        .when('/login', {
            templateUrl: 'partials/login/login.html',
            controller: 'LoginCtrl'
    });

    $locationProvider.html5Mode(false).hashPrefix('!');

}]);