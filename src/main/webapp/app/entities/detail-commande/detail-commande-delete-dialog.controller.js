(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('DetailCommandeDeleteController',DetailCommandeDeleteController);

    DetailCommandeDeleteController.$inject = ['$uibModalInstance', 'entity', 'DetailCommande'];

    function DetailCommandeDeleteController($uibModalInstance, entity, DetailCommande) {
        var vm = this;

        vm.detailCommande = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DetailCommande.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
