'use strict';

// App module

var relativelySocialApp = angular.module('relativelySocialApp', ['ngRoute', 'relativelySocialControllers']);

relativelySocialApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/', {
            templateUrl: 'partials/main.html',
            controller: 'MainCtrl'
    });

}]);