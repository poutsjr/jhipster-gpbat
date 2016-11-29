(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('TypeCommande', TypeCommande);

    TypeCommande.$inject = ['$resource'];

    function TypeCommande ($resource) {
        var resourceUrl =  'api/type-commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
