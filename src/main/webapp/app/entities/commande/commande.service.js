(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('Commande', Commande);

    Commande.$inject = ['$resource', 'DateUtils'];

    function Commande ($resource, DateUtils) {
        var resourceUrl =  'api/commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateEdition = DateUtils.convertLocalDateFromServer(data.dateEdition);
                        data.dateReception = DateUtils.convertLocalDateFromServer(data.dateReception);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateEdition = DateUtils.convertLocalDateToServer(copy.dateEdition);
                    copy.dateReception = DateUtils.convertLocalDateToServer(copy.dateReception);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateEdition = DateUtils.convertLocalDateToServer(copy.dateEdition);
                    copy.dateReception = DateUtils.convertLocalDateToServer(copy.dateReception);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
