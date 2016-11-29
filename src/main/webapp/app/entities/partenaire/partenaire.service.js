(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('Partenaire', Partenaire);

    Partenaire.$inject = ['$resource'];

    function Partenaire ($resource) {
        var resourceUrl =  'api/partenaires/:id';

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
