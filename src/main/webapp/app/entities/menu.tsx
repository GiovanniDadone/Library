import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/autore">
        Autore
      </MenuItem>
      <MenuItem icon="asterisk" to="/libro">
        Libro
      </MenuItem>
      <MenuItem icon="asterisk" to="/recensione">
        Recensione
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
