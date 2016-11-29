(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ProduitPartenaireDialogController', ProduitPartenaireDialogController);

    ProduitPartenaireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProduitPartenaire', 'Bordereau'];

    function ProduitPartenaireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProduitPartenaire, Bordereau) {
        var vm = this;

        vm.produitPartenaire = entity;
        vm.clear = clear;
        vm.save = save;
        vm.bordereaus = Bordereau.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.produitPartenaire.id !== null) {
                ProduitPartenaire.update(vm.produitPartenaire, onSaveSuccess, onSaveError);
            } else {
                ProduitPartenaire.save(vm.produitPartenaire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:produitPartenaireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
