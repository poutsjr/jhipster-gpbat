(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('DiagnosticChantierSearch', DiagnosticChantierSearch);

    DiagnosticChantierSearch.$inject = ['$resource'];

    function DiagnosticChantierSearch($resource) {
        var resourceUrl =  'api/_search/diagnostic-chantiers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
