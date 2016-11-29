(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('Simulation', Simulation);

    Simulation.$inject = ['$resource', 'DateUtils'];

    function Simulation ($resource, DateUtils) {
        var resourceUrl =  'api/simulations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateSimulation = DateUtils.convertLocalDateFromServer(data.dateSimulation);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateSimulation = DateUtils.convertLocalDateToServer(copy.dateSimulation);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateSimulation = DateUtils.convertLocalDateToServer(copy.dateSimulation);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
