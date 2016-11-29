(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('AvancementChantierDialogController', AvancementChantierDialogController);

    AvancementChantierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AvancementChantier', 'Chantier'];

    function AvancementChantierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AvancementChantier, Chantier) {
        var vm = this;

        vm.avancementChantier = entity;
        vm.clear = clear;
        vm.save = save;
        vm.chantiers = Chantier.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.avancementChantier.id !== null) {
                AvancementChantier.update(vm.avancementChantier, onSaveSuccess, onSaveError);
            } else {
                AvancementChantier.save(vm.avancementChantier, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:avancementChantierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
