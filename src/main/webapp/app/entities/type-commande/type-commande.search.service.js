(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('TypeCommandeSearch', TypeCommandeSearch);

    TypeCommandeSearch.$inject = ['$resource'];

    function TypeCommandeSearch($resource) {
        var resourceUrl =  'api/_search/type-commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
