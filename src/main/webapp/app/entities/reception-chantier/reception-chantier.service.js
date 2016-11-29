(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('ReceptionChantier', ReceptionChantier);

    ReceptionChantier.$inject = ['$resource'];

    function ReceptionChantier ($resource) {
        var resourceUrl =  'api/reception-chantiers/:id';

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
