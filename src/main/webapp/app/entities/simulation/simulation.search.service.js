(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('SimulationSearch', SimulationSearch);

    SimulationSearch.$inject = ['$resource'];

    function SimulationSearch($resource) {
        var resourceUrl =  'api/_search/simulations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
