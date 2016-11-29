(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('DetailCommandeDialogController', DetailCommandeDialogController);

    DetailCommandeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DetailCommande', 'Simulation', 'Commande', 'Bordereau'];

    function DetailCommandeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DetailCommande, Simulation, Commande, Bordereau) {
        var vm = this;

        vm.detailCommande = entity;
        vm.clear = clear;
        vm.save = save;
        vm.simulations = Simulation.query();
        vm.commandes = Commande.query();
        vm.bordereaus = Bordereau.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.detailCommande.id !== null) {
                DetailCommande.update(vm.detailCommande, onSaveSuccess, onSaveError);
            } else {
                DetailCommande.save(vm.detailCommande, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:detailCommandeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
