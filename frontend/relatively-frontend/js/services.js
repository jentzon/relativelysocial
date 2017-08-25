'use strict';

var authService = angular.module('authService', [ngResource]);

const LOGIN_PATH = "/login";
const VALIDATION_PATH = "/validate";

const SERVER_IP = "139.162.194.94";
const SERVICE_PORT = "4501";

authService.factory('Login', ['$resource', function($resource) {
    return $resource("http://" + SERVER_IP + LOGIN_PATH + ":" + SERVICE_PORT, {}, {
        post: {method: 'POST', cache: false, isArray: false}
    });
}]);