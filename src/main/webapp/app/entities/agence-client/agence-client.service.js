(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('AgenceClient', AgenceClient);

    AgenceClient.$inject = ['$resource'];

    function AgenceClient ($resource) {
        var resourceUrl =  'api/agence-clients/:id';

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
