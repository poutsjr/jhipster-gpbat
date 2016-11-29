(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('UtilisateurSearch', UtilisateurSearch);

    UtilisateurSearch.$inject = ['$resource'];

    function UtilisateurSearch($resource) {
        var resourceUrl =  'api/_search/utilisateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
