(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('DetailCommande', DetailCommande);

    DetailCommande.$inject = ['$resource'];

    function DetailCommande ($resource) {
        var resourceUrl =  'api/detail-commandes/:id';

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
