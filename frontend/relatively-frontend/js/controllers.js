'use strict';

// Controllers

var relativelySocialControllers = angular.module('relativelySocialControllers', []);

relativelySocialControllers.controller('MainCtrl', ['$scope', function MainCtrl($scope) {
    $scope.mainCtrlStatus = 'Main controller running...';
}]);