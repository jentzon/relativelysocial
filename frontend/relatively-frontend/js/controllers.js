'use strict';

// Controllers

var relativelySocialControllers = angular.module('relativelySocialControllers', []);

relativelySocialControllers.controller('MainCtrl', ['$scope', function MainCtrl($scope) {
    console.log('Main controller loaded...');
}]);

relativelySocialControllers.controller('LoginCtrl', ['$scope', function LoginCtrl($scope) {
    console.log('Login controller loaded...');
}]);