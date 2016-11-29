(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ProduitPartenaireDeleteController',ProduitPartenaireDeleteController);

    ProduitPartenaireDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProduitPartenaire'];

    function ProduitPartenaireDeleteController($uibModalInstance, entity, ProduitPartenaire) {
        var vm = this;

        vm.produitPartenaire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProduitPartenaire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
