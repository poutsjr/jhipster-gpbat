(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('ProduitPartenaireSearch', ProduitPartenaireSearch);

    ProduitPartenaireSearch.$inject = ['$resource'];

    function ProduitPartenaireSearch($resource) {
        var resourceUrl =  'api/_search/produit-partenaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
