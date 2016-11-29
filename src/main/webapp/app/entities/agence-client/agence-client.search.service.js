(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('AgenceClientSearch', AgenceClientSearch);

    AgenceClientSearch.$inject = ['$resource'];

    function AgenceClientSearch($resource) {
        var resourceUrl =  'api/_search/agence-clients/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
